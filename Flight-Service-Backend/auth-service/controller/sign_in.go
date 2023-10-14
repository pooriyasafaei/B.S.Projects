package controller

import (
	"auth-service/database/models"
	"auth-service/utils/jwt"
	"errors"
	"golang.org/x/crypto/bcrypt"
)

type SignInModel struct {
	Password string `json:"password"  binding:"required"`
	Data     string `json:"data"  binding:"required,email|e164"`
	IsPhone  bool   `json:"IsPhone" binding:"required"`
}

func SignIn(model *SignInModel) (*TokenModel, error) {
	querySearch := ""
	if model.IsPhone {
		querySearch = "phone_number = ?"
	} else {
		querySearch = "email = ?"
	}

	user := models.GetUser(querySearch, model.Data)

	if user == nil {
		return nil, errors.New("credential data is invalid")
	}

	if err := bcrypt.CompareHashAndPassword([]byte(user.PasswordHash), []byte(model.Password)); err != nil {
		return nil, errors.New("credential data is invalid")
	}

	accessToken, err := jwt.GenerateToken(user, jwt.ACCESS)
	refreshToken, err2 := jwt.GenerateToken(user, jwt.REFRESH)

	if err != nil || err2 != nil {
		return nil, errors.New("can not create tokens for your user")
	}

	return &TokenModel{RefreshToken: refreshToken, AccessToken: accessToken}, nil
}
