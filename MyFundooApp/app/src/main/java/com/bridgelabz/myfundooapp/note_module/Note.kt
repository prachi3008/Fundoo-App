package com.bridgelabz.myfundooapp.note_module

data class Note(
    var nodeId: Int,
    var nodeTitle: String,
    var nodeDesc: String,
    var nodeDate: String,
    var nodeColor: Int,
    var isReminder: Boolean,
    var isArchieve: Boolean,
    var isImportant: Boolean,
    var isDelete: Boolean,
    var position: Int
)
//    var nodeId : Int = nodeId
//    var nodeTitle : String = nodeTitle
//    var nodeDesc : String = nodeDesc
//    var nodeDate : String = nodeDate
//    var nodeColor : Int = nodeColor
//    var isArchieve : Boolean = isArchieve
//    var isImportant : Boolean = isImportant
//    var isReminder : Boolean = isReminder
//    var isDelete : Boolean = isDelete
