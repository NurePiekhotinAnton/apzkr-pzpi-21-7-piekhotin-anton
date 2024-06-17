package com.example.apz_proj_mobile.constant

enum class URL {
    BASE_URL {
        override fun toString(): String {
            return "http://10.0.2.2:8080/api/v1/"
        }
    }
}