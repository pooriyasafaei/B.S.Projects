package controller

import (
	"auth-service/database/models"
	"auth-service/utils/jwt"
)

type UserAccountInfo struct {
	Id          int64
	Email       string
	PhoneNumber string
	Male        bool
	FirstName   string
	LastName    string
}

func UserInfo(token string) (*UserAccountInfo, error) {
	userEmail, err, _ := jwt.VerifyTokenAndGetUserEmail(token, jwt.ACCESS)
	if err != nil {
		return nil, err
	}
	user := models.GetUser("email = ?", userEmail)
	if user != nil {
		return &UserAccountInfo{
			Id:          user.ID,
			Email:       user.Email,
			PhoneNumber: user.PhoneNumber,
			Male:        user.Male,
			FirstName:   user.FirstName,
			LastName:    user.LastName,
		}, nil
	}
	return nil, nil
}
