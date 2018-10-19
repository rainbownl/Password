package com.niulei.password.content

class ContentItem() {
    constructor(in_title: String, in_username: String, in_password: String): this(){
        this.title = in_title
        this.userName = in_username
        this.password = in_password
    }
    var id : Int = 0
    var title: String? = null
    var userName: String? = null
    var password: String? = null
}