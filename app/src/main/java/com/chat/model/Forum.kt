package com.chat.model

import java.util.Objects

class Forum {
    var id: Int = 0
    var name: String = ""
    var imageUrl: String = ""
    var description: String = ""
    var terms: String = ""
    var creator: Any = Any() // Use Any as a generic type, or specify the actual class
    var categories: String = ""
}
