package com.example.finalprojecthobbiesconnect.utilties

class Constants {
    companion object{


        const val NO_FRIENDS_TEXT: String="You Don\'t Have Friends Right Now"
        const val NO_FRIEND_FOUND_TEXT: String="No Friends Found"
        const val STARTER_DEFAULT_MESSAGE:String="Hello, I just accept your friend request"
        const val FRIEND_REQUEST_MESSAGE:String="sent you a friend request"
        const val ALERT1 :String ="email and password missing!"
        const val ALERT2 :String ="email missing!"
        const val ALERT3 :String ="password missing!"
        const val ALERT_LOAD_CHATS :String ="Failed to load chats details"
        const val ALERT_LOAD_FRIENDS :String ="Failed to load friends"
        const val ALERT_LOAD_USERS :String ="Failed to load users"
        const val ALERT_LOAD_USER_PROFILE :String ="Failed to load user profile photo"
        const val MESSAGE_NO_CHANGES_PHOTO :String ="No profile photo changes"
        const val MESSAGE_PHOTO_UPDATED :String ="Profile photo updated successfully"
        const val MESSAGE_HOBBIES_UPDATED :String ="Hobbies updated successfully"
        const val ALERT_UPDATE_HOBBIES :String ="Failed to update hobbies"
        const val HOBBIES_LIMIT_MESSAGE :String ="You can't select more than 5 hobbies"
        const val BACKGROUND_SOUND_TAG :String ="BackgroundSound"
        const val PENDING_FRIEND_ADAPTER_TAG :String ="PendingFriendAdapter"
        const val PARTICIPATE_STATUS_UPDATED_MESSAGE :String ="Participate status updated successfully"
        const val CHAT_ROOM_ACTIVITY_TAG :String ="ChatRoomActivity"
        const val ALERT_LOAD_USER :String ="Failed to load user data"
        const val ALERT_USER_NOT_FOUND :String ="User not found"
        const val SIGN_IN_SUCCESSFUL :String ="Sign in successful!"
        const val ALERT_SAVE_USER :String ="Failed to save user details"
        const val ALERT_LOAD_CHANGES :String ="Failed to load changes"
        const val REGISTER_SUCCESSFUL :String ="User registered successfully"
        const val ENTER_NAME_MESSAGE :String="Please enter your name"
        const val ENTER_EMAIL_MESSAGE :String="Please enter your email"
        const val ENTER_PASSWORD_MESSAGE :String="Please enter your password"
        const val ENTER_CONFIRM_PASSWORD_MESSAGE :String="Please confirm your password"
        const val PASSWORD_MISMATCH_MESSAGE :String="Passwords don't match"
        const val FAILED_REMOVE_PENDING_FRIEND :String="Failed to remove friend from pending list"



        const val TODAYS_DATE :String ="Today"
        const val  YEAR_DIFFER:Int =120
        const val  HOBBIES_LIMIT:Int =5


        const val DATA_KEY: String="SP_FILE"
        const val USERID_KEY: String="USERID"

        const val UNKNOWN_USER :String ="Unknown User"
        const val PARTNER_NOT_FOUND :String ="partner not found"

          val HOBBIES_LIST :List<String> =arrayListOf("Gaming", "Reading",
             "Cycling","Basketball","Soccer","Tennis","Hiking","Painting",
             "Food","Movies","Catan","Skating","Surfing","Yoga","Netflix & Chill",
             "Chess","Music","Dancing","Running","Bowling","Archery","Fishing","FIFA","Pool","Camping"
         )
        const val SEARCH_FRAGMENT  :Int=1
        const val NOTIFICATIONS_FRAGMENT  :Int=2
        const val PROFILE_ACTIVITY  :Int=3
        const val FRIEND_LIST_FRAGMENT  :Int=4
        const val CHATS_FRAGMENT  :Int=5
        const val CHAT_ROOM_ACTIVITY  :Int=6
        const val NAVIGATION_KEY:String="navigation"
        const val NAVIGATION_KEY2:String="navigation2"


        const val CHATS_REF:String="chats"
        const val USERS_REF:String="users"
        const val PROFILE_PHOTO_REF:String="profilePhoto"
        const val HOBBIES_REF:String="hobbies"
        const val MESSAGES_REF:String="messages"
        const val PARTICIPANTS_STATUS_REF:String="participantsStatus"
        const val READ_PENDING_REF:String="readPend"
        const val PENDING_FRIENDS_LIST_REF:String="pendingFriendsList"
        const val FRIENDS_LIST_REF:String="friendsList"







    }
}