package jwt

import (
	"auth-service/redis"
	redisUtils "auth-service/utils/redis"
	"errors"

	"github.com/golang-jwt/jwt/v4"
	"time"
)

func VerifyTokenAndGetUserEmail(jwtToken string, tokenType TokenType) (string, error, int64) {
	// check from redis
	redisKey := redisUtils.CreateUnauthorizedRedisKey(jwtToken)
	forcedExpired := redis.Client.Get(redis.Ctx, redisKey).Val()

	if forcedExpired == "1" {
		return "", errors.New("token has been expired"), -1
	}

	token, _ := jwt.Parse(jwtToken, func(token *jwt.Token) (interface{}, error) {
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, errors.New("invalid token")
		}
		return token, nil
	})

	if token == nil {
		return "", errors.New("invalid token"), -1
	}

	claims, ok := token.Claims.(jwt.MapClaims)
	if !ok {
		return "", errors.New("invalid token"), -1
	}

	parsedTokenType := claims["tokenType"]

	if parsedTokenType != tokenType.String() {
		return "", errors.New("invalid token"), -1
	}

	exp := claims["exp"].(float64)
	if int64(exp) < time.Now().Local().Unix() {
		return "", errors.New("token has been expired"), -1
	}

	userEmail := claims["userEmail"].(string)

	return userEmail, nil, int64(exp)
}
