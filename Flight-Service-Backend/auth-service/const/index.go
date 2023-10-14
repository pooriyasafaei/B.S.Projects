package _const

import "os"

var ProjectSecret []byte
var Port string
var ServerPemLocation string
var ServerKeyPemLocation string

func InitConsts() {
	ProjectSecret = []byte(os.Getenv("ProjectSecret"))
	Port = os.Getenv("PORT")
	ServerPemLocation = os.Getenv("ServerPemLocation")
	ServerKeyPemLocation = os.Getenv("ServerKeyPemLocation")
}
