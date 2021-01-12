#! /bin/bash

####
#  1,  创建SDK - Data
#  2,  创建SDK - Form
#  3,  创建Entity -
#  4,  创建Repository -
#  5,  创建Service
#  6,  创建Converter
#  7,  创建Facade
#  8,  创建Controller
#############

######## Template Variable begin ########
rootPackage=tech.icoding.sci
######## Template Variable End ####3
read -p 'Package: ' package
read -p 'Entity Name: ' domain

## sdk/src/main/java/tech/icoding/sci/sdk/data/

#### 1 创建 SDK Data #################
echo "创建 SDK Data **********"
sh xbox/data.sh "$package" "$domain"

echo "创建SDK - Form"
sh xbox/form.sh "$package" "$domain"

echo "创建Entity"
sh xbox/entity.sh "$package" "$domain"

echo "创建Repository"
sh xbox/repository.sh "$package" "$domain"

echo "创建Service"
sh xbox/service.sh "$package" "$domain"

echo "创建Converter"
sh xbox/converter.sh "$package" "$domain"

echo "创建Facade"
sh xbox/facade.sh "$package" "$domain"

echo "创建Controller"
sh xbox/controller.sh "$package" "$domain"
