package models

import (
	"auth-service/database"
	"time"
)

type UnauthorizedToken struct {
	UserID     int64 `gorm:"foreignKey:;constraint:OnUpdate:CASCADE,OnDelete:SET NULL;"`
	User       UserAccount
	Token      string `gorm:"index;unique"`
	Expiration time.Time
}

func AddUnauthorizedToken(
	userId int64,
	token string,
	expiration time.Time,
) error {
	return database.DB.Create(&UnauthorizedToken{
		UserID:     userId,
		Token:      token,
		Expiration: expiration,
	}).Error
}
