package routes

import (
	"auth-service/controller"
	"auth-service/utils/rest"
	"github.com/gin-gonic/gin"
	"net/http"
)

func SignOut(c *gin.Context) {

	accessToken := rest.GetTokenFromCtxHeader(c)

	var model controller.RefreshTokenModel

	if err := c.ShouldBindJSON(&model); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	err := controller.SignOut(
		&controller.TokenModel{
			RefreshToken: model.Token,
			AccessToken:  accessToken,
		},
	)

	if err != nil {
		c.JSON(http.StatusUnauthorized, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, "success")
}
