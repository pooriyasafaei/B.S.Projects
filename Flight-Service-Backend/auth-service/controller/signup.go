package controller

import (
	"auth-service/database/models"
	"errors"
	"golang.org/x/crypto/bcrypt"
)

type SignupModel struct {
	Password    string `json:"password"  binding:"required"`
	Email       string `json:"email"  binding:"required,email"`
	PhoneNumber string `json:"phoneNumber" binding:"required,e164"`
	Male        bool   `json:"male" binding:"required"`
	FirstName   string `json:"firstName" binding:""`
	LastName    string `json:"lastName" binding:""`
}

func Signup(model *SignupModel) error {

	passwordHash, err := bcrypt.GenerateFromPassword([]byte(model.Password), bcrypt.DefaultCost)

	if err != nil {
		return errors.New("can not create proper password for you")
	}

	return models.AddUser(
		model.Email,
		model.PhoneNumber,
		model.Male,
		model.FirstName,
		model.LastName,
		string(passwordHash),
	)
}
