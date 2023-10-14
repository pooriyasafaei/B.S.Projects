package models

import (
	"auth-service/database"
)

type UserAccount struct {
	ID           int64  `gorm:"primary_key;auto_increment;not_null"`
	Email        string `gorm:"unique;not null;default:null;uniqueIndex"`
	PhoneNumber  string `gorm:"unique;not null;default:null;uniqueIndex"`
	Male         bool   `gorm:"type:bool"`
	FirstName    string
	LastName     string
	PasswordHash string
}

func AddUser(
	email string,
	phoneNumber string,
	male bool,
	firstName string,
	lastName string,
	passwordHash string,
) error {
	return database.DB.Create(&UserAccount{
		Email:        email,
		PhoneNumber:  phoneNumber,
		Male:         male,
		FirstName:    firstName,
		LastName:     lastName,
		PasswordHash: passwordHash,
	}).Error
}

func GetUser(
	query string,
	data string,
) *UserAccount {
	user := UserAccount{}
	if database.DB.Where(query, data).First(&user).Error != nil {
		return nil
	}
	return &user
}
