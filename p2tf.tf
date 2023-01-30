# Use creds
provider "aws" {
  access_key = "AKIAR6QHH7EI4CE3CEJE"
  secret_key = "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"
  region     = "eu-central-1"
}

# Create vpc
resource "aws_vpc" "task2m-vpc" {
  cidr_block = "10.0.0.0/16"
  tags = {
    Name = "task2m"
  }
}

# Create key
resource "tls_private_key" "rsa" {
  algorithm = "RSA"
  rsa_bits  = 4096
}

resource "local_file" "task2m-key" {
  content  = tls_private_key.rsa.private_key_pem
  filename = "task2m-key"
}

resource "aws_key_pair" "task2m-key" {
  key_name   = "task2m-key"
  public_key = tls_private_key.rsa.public_key_openssh
}


resource "aws_internet_gateway" "gw" {
  vpc_id = aws_vpc.task2m-vpc.id
}

resource "aws_route_table" "task2m-route-table" {
  vpc_id = aws_vpc.task2m-vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.gw.id
  }

  tags = {
    Name = "task2m"
  }
}

# Create subnet
resource "aws_subnet" "task2m-sn" {
  vpc_id            = aws_vpc.task2m-vpc.id
  cidr_block        = "10.0.1.0/28"
  availability_zone = "eu-central-1a"

  tags = {
    Name = "task2m-sn"
  }
}

# Associate subnet with Route Table
resource "aws_route_table_association" "a" {
  subnet_id      = aws_subnet.task2m-sn.id
  route_table_id = aws_route_table.task2m-route-table.id
}

# Create Security Group to allow port 22,80,443
resource "aws_security_group" "allow_web" {
  name        = "allow_web_traffic"
  description = "Allow Web inbound traffic" 
  vpc_id      = aws_vpc.task2m-vpc.id

  ingress {
    description = "SSH"
    from_port   = var.ssh_port
    to_port     = var.ssh_port
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "task2m_allow_ssh"
  }
  
}

# Create a network interface with an ip in the subnet that was created in step 4
resource "aws_network_interface" "web-server-nic" {
  subnet_id       = aws_subnet.task2m-sn.id
  private_ips     = ["10.0.1.50"]
  security_groups = [aws_security_group.allow_web.id]

}

# Assign an elastic IP to the network interface created in step 7
resource "aws_eip" "one" {
  vpc                       = true
  network_interface         = aws_network_interface.web-server-nic.id
  associate_with_private_ip = "10.0.1.50"
  depends_on                = [aws_internet_gateway.gw]
}

# Read port from file
data "template_file" "num" {
  template = "${file("num")}"
}

# Create instance
resource "aws_instance" "task2m" {
  ami           = "ami-0039da1f3917fa8e3"
  instance_type = "t2.micro"
  availability_zone = "eu-central-1a"
  key_name = aws_key_pair.task2m-key.key_name
  network_interface {
    device_index         = 0
    network_interface_id = aws_network_interface.web-server-nic.id
  }
  # Change SSH port
  user_data = <<-EOF
                #!/bin/bash
                sudo sed -i 's/^#Port.*/Port ${data.template_file.num.rendered}/' /etc/ssh/sshd_config && sudo systemctl restart ssh
                EOF
}

# Output info
output "public_ip" {
  value = aws_eip.one.public_ip
}

output "data" {
  value = "${data.template_file.num.rendered}"
}

