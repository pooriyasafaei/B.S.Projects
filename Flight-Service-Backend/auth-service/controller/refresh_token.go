package controller

import (
	"auth-service/database/models"
	"auth-service/utils/jwt"
	"errors"
)

type RefreshTokenModel struct {
	Token string `json:"refresh" binding:"required"`
}

func RefreshToken(model *RefreshTokenModel) (*TokenModel, error) {
	userEmail, err, _ := jwt.VerifyTokenAndGetUserEmail(model.Token, jwt.REFRESH)
	if err != nil {
		return nil, err
	}
	user := models.GetUser("email = ?", userEmail)

	if user == nil {
		return nil, errors.New("credential data is invalid")
	}

	accessToken, err1 := jwt.GenerateToken(user, jwt.ACCESS)
	refreshToken, err2 := jwt.GenerateToken(user, jwt.REFRESH)

	if err1 != nil || err2 != nil {
		return nil, errors.New("can not create tokens for your user")
	}

	return &TokenModel{RefreshToken: refreshToken, AccessToken: accessToken}, nil
}
