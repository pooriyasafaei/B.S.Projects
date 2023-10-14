package jwt

import (
	_const "auth-service/const"
	"auth-service/database/models"
	"github.com/golang-jwt/jwt/v4"
	"time"
)

type TokenType int64

const (
	ACCESS TokenType = iota
	REFRESH
)

func (t TokenType) String() string {
	switch t {
	case ACCESS:
		return "access"
	case REFRESH:
		return "refresh"
	}
	return "unknown"
}

func (t TokenType) exp() int64 {
	now := time.Now()
	switch t {
	case ACCESS:
		return now.Add(10 * time.Minute).Unix()
	case REFRESH:
		return now.Add(1 * time.Hour).Unix()
	}
	return now.Unix()
}

func GenerateToken(user *models.UserAccount, tokenType TokenType) (string, error) {
	token := jwt.New(jwt.SigningMethodHS256)
	claims := token.Claims.(jwt.MapClaims)
	claims["tokenType"] = tokenType.String()
	claims["exp"] = tokenType.exp()
	claims["authorized"] = true
	claims["userEmail"] = user.Email

	tokenString, err := token.SignedString(_const.ProjectSecret)

	if err != nil {
		return "", err
	}

	return tokenString, nil
}
