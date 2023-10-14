package routes

import (
	"auth-service/controller"
	"github.com/gin-gonic/gin"
	"net/http"
)

func Signup(c *gin.Context) {
	var model controller.SignupModel
	if err := c.ShouldBindJSON(&model); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	if err := controller.Signup(&model); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusCreated, "success")
}
