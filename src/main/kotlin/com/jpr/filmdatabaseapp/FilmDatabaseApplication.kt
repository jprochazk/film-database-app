package com.jpr.filmdatabaseapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FilmDatabaseApplication {
	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			runApplication<FilmDatabaseApplication>(*args)
		}
	}
}
