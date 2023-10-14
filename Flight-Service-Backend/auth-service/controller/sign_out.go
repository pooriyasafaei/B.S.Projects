package controller

import (
	"auth-service/database/models"
	"auth-service/utils/jwt"
	"errors"
)

func SignOut(model *TokenModel) error {
	userEmail, err, expUnixTime := jwt.VerifyTokenAndGetUserEmail(model.AccessToken, jwt.ACCESS)

	if err != nil {
		return err
	}

	userEmailViaRefreshToken, err1, refreshExpUnixTime := jwt.VerifyTokenAndGetUserEmail(model.RefreshToken, jwt.REFRESH)

	if err1 != nil {
		return err1
	}

	if userEmail != userEmailViaRefreshToken {
		return errors.New("tokens are invalid")
	}

	user := models.GetUser("email = ?", userEmail)

	if user == nil {
		return errors.New("you can not logout")
	}

	if err = UnAuthorizedSingleToken(user.ID, model.AccessToken, expUnixTime); err != nil {
		return err
	}
	if err = UnAuthorizedSingleToken(user.ID, model.RefreshToken, refreshExpUnixTime); err != nil {
		return err
	}

	return nil
}
