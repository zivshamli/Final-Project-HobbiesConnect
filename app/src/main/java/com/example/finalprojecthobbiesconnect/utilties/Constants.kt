package com.example.finalprojecthobbiesconnect.utilties

class Constants {
    companion object{

        const val STARTER_DEFUALT_MASSEGE:String="Hello, I just accept your friend request"
        const val FRIENDREQUESTMESSAGE:String="sent you a friend request"
        const val ALRET1 :String ="email and password missing!"
        const val ALRET2 :String ="email missing!"
        const val ALRET3 :String ="password missing!"
        const val TODAYS_DATE :String ="Today"
        const val  YEAR_DIFFER:Int =120


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
        const val NAVIGATION_KEY:String="navigation"
        const val NAVIGATION_KEY2:String="navigation2"


    }
}