package com.example.finalprojecthobbiesconnect.utilties

class Constants {
    companion object{

        const val STARTER_DEFUALT_MASSEGE:String="Hello, I just accept your friend request"
        const val FRIENDREQUESTMESSAGE:String="sent you a friend request"
        const val ALRET1 :String ="email and password missing!"
        const val ALRET2 :String ="email missing!"
        const val ALRET3 :String ="password missing!"

          val HOBBIES_LIST :List<String> =arrayListOf("Gaming", "Reading",
             "Cycling","Basketball","Soccer","Tennis","Hiking","Painting",
             "Food","Movies","Catan","Skating","Surfing","Yoga","Netflix & Chill",
             "Chess","Music","Dancing","Running","Bowling","Archery","Fishing","FIFA","Pool","Camping"
         )
        const val SEARCH_FRAGMENT  :Int=1
        const val NOTIFICATIONS_FRAGMENT  :Int=2
        const val PROFILE_ACTIVITY  :Int=3
        const val FRIEND_LIST_FRAGMENT  :Int=4
        const val NAVIGATION_KEY:String="navigation"
        const val NAVIGATION_KEY2:String="navigation2"

    }
}