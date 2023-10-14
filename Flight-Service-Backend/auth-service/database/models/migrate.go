package models

import "auth-service/database"

func MigrateDatabase() error {
	if err := database.DB.AutoMigrate(&UserAccount{}); err != nil {
		return nil
	}
	if err := database.DB.AutoMigrate(&UnauthorizedToken{}); err != nil {
		return nil
	}
	return nil
}
