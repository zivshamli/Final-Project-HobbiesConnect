# HobbiesConnect

HobbiesConnect is a social network that helps you find friends based on shared hobbies. Whether you're looking for partners for specific activities or games, HobbiesConnect will help you connect with people who share your interests.

## Key Features

- Create and manage your user profile
- List your favorite hobbies
- Add and search for friends using hobby-related filters
- Chats friends
  
## Server Uses
- Firebase Authentication: for login management and user registration.
- Firebase Firestore: for managing users' data (profiles, hobbies, chat messages).
- Firebase Storage: for storing photos that users upload (for example, profile).



## Icon  
![HobbiesConnect Icon](https://raw.githubusercontent.com/zivshamli/Final-Project-HobbiesConnect/refs/heads/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.webp)


## Login Screen  
The login screen allows users to sign in with their existing account credentials or register for a new account. It includes input fields for email and password and user registration.
I use Firebase Authentication to store emails and passwords

<img src="https://github.com/zivshamli/photosForHobbiesConnect/blob/main/%D7%AA%D7%9E%D7%95%D7%A0%D7%94%20%D7%A9%D7%9C%20WhatsApp%E2%80%8F%202024-10-01%20%D7%91%D7%A9%D7%A2%D7%94%2023.15.47_a5f152bf.jpg" alt="HobbiesConnect Login Screen" width="300" height="500"/>

## Sign Up Screen

The sign-up screen in HobbiesConnect allows users to register with essential information and upload a profile picture. Upon launching this screen, users are prompted to:

- **Grant Permissions**: Users must grant permission to upload a profile picture from their gallery. If granted, they can select an image, which is stored in Firebase Storage.
- **Profile Picture Display**: After selecting the profile picture, the app uses the **Glide** library to load and display the image efficiently in the UI.
- **Input Fields**: Users enter their email, password, and confirm their password.
- **Select Birth Year**: Users specify their year of birth.
- **Choose Hobbies**: Users select up to 5 hobbies. If more than five are selected, a toast message will appear, reminding them to choose only up to 5 hobbies. A summary list of chosen hobbies is also displayed.

Once all information is filled out, users tap the **Save** button to complete registration. A loading lottie appears while the app saves the data and then transitions to the main navigation screen.

### Sign Up Screen Preview

<div style="display: flex; justify-content: space-around;">
    <img src="https://github.com/zivshamli/photosForHobbiesConnect/blob/main/%D7%AA%D7%9E%D7%95%D7%A0%D7%94%20%D7%A9%D7%9C%20WhatsApp%E2%80%8F%202024-11-06%20%D7%91%D7%A9%D7%A2%D7%94%2016.57.42_0a38e727.jpg" alt="Sign Up Screen - Step 1" width="300" height="500"/>
    <img src="https://github.com/zivshamli/photosForHobbiesConnect/blob/main/%D7%AA%D7%9E%D7%95%D7%A0%D7%94%20%D7%A9%D7%9C%20WhatsApp%E2%80%8F%202024-11-06%20%D7%91%D7%A9%D7%A2%D7%94%2016.57.42_5364f3a3.jpg" alt="Sign Up Screen - Step 2" width="300" height="500"/>
</div>

## Navigation Screen

The main navigation screen in HobbiesConnect provides users with quick access to all key features through a menu with five main sections:

1. **Edit Profile**: Allows users to update their personal information, hobbies, and profile picture.
2. **Friends List**: Displays the user’s list of friends, where they can view profiles and initiate chats.
3. **User Search**: Users can search for other people based on hobbies, allowing them to connect with others who share similar interests.
4. **Friend Requests**: Shows incoming friend requests, where users can accept or decline connections.
5. **Chats List**: Displays ongoing chats with friends, enabling users to continue conversations from where they left off.

### Navigation Screen Preview

<div style="display: flex; justify-content: space-around;">
    <img src="https://github.com/zivshamli/photosForHobbiesConnect/blob/main/%D7%AA%D7%9E%D7%95%D7%A0%D7%94%20%D7%A9%D7%9C%20WhatsApp%E2%80%8F%202024-11-06%20%D7%91%D7%A9%D7%A2%D7%94%2017.51.23_889c9e29.jpg" alt="Navigation Screen" width="300" height="500"/>
</div>


## User Search Fragment

The **User Search Fragment** in HobbiesConnect provides users with powerful search filters to find new friends based on shared hobbies, name, and age range. This feature allows users to easily connect with people who have similar interests and meet their specific preferences.

### Search Filters
- **Hobby-Based Search**: Users can filter results by selecting one or more hobbies. This enables users to find others with matching interests.
- **Name Search**: Users can search for friends by entering a specific name, helping to locate individuals directly.
- **Age Range Filter**: Users can set a minimum and maximum age to narrow down results to a particular age group, making it easier to connect with peers.

Once search filters are set, the app displays a list of matching users, allowing easy access to profiles for viewing details and sending friend requests.

### User Search Fragment Preview
<div style="display: flex; justify-content: space-around;">
    <img src="https://github.com/zivshamli/photosForHobbiesConnect/blob/main/%D7%AA%D7%9E%D7%95%D7%A0%D7%94%20%D7%A9%D7%9C%20WhatsApp%E2%80%8F%202024-11-06%20%D7%91%D7%A9%D7%A2%D7%94%2017.51.23_889c9e29.jpg" alt="Navigation Screen" width="300" height="500"/>
</div>


## Other User Profile Page

The **Other User Profile Page** in HobbiesConnect allows users to view the profile details of other members, providing insights into their interests and a pathway to connect or chat.

### Key Features
- **Profile Information**: Displays the user’s name, profile picture, age, and additional details (if provided) to give a fuller sense of their profile.
- **Hobbies List**: Shows a list of the user’s hobbies, making it easy to identify shared interests.
- **Friend Request Button**: If the user is not yet a friend, there is an option to send a friend request directly from this page.
- **Chat Button**: If the user is already a friend, a button to start a chat appears, enabling direct communication.

These features facilitate meaningful connections and provide easy access to start a conversation or connect based on shared hobbies.

### Other User Profile Page Preview

<div style="display: flex; justify-content: space-around;">
    <img src="https://github.com/zivshamli/photosForHobbiesConnect/blob/main/%D7%AA%D7%9E%D7%95%D7%A0%D7%94%20%D7%A9%D7%9C%20WhatsApp%E2%80%8F%202024-11-06%20%D7%91%D7%A9%D7%A2%D7%94%2018.28.39_b76922a5.jpg" alt="Other User Profile - Overview" width="300" height="500"/>
    <img src="https://github.com/zivshamli/photosForHobbiesConnect/blob/main/%D7%AA%D7%9E%D7%95%D7%A0%D7%94%20%D7%A9%D7%9C%20WhatsApp%E2%80%8F%202024-11-06%20%D7%91%D7%A9%D7%A2%D7%94%2017.51.23_81cd5ad0.jpg" alt="Other User Profile - Chat or Friend Request" width="300" height="500"/>
</div>


## Friend Requests Fragment

The **Friend Requests Fragment** in HobbiesConnect allows users to view and manage incoming friend requests from other users, making it simple to accept or decline new connections.

### Key Features
- **List of Incoming Requests**: Displays a list of users who have sent friend requests, including their profile picture and name for easy identification.
- **Accept Button**: Allows the user to accept a friend request, instantly adding the sender to their friends list.
- **Automatic Icebreaker Message**: When a user accepts a friend request, the app automatically sends an initial chat message on behalf of the accepting user to the new friend. This friendly "icebreaker" message helps start a conversation and encourages interaction.
- **Decline Button**: Allows the user to decline a friend request if they choose not to connect.
- **Real-Time Updates**: The list of requests updates automatically, reflecting any new incoming friend requests as they arrive.

This fragment enhances friend management by providing a smooth, welcoming way to connect with new friends in the community.

### Friend Requests Fragment Preview

<div style="display: flex; justify-content: space-around;">
    <img src="https://github.com/zivshamli/photosForHobbiesConnect/blob/main/%D7%AA%D7%9E%D7%95%D7%A0%D7%94%20%D7%A9%D7%9C%20WhatsApp%E2%80%8F%202024-11-06%20%D7%91%D7%A9%D7%A2%D7%94%2017.56.15_f062e242.jpg" alt="Friend Requests Fragment - Incoming Requests" width="300" height="500"/>
   
</div>

## Friends List Fragment

The **Friends List Fragment** in HobbiesConnect allows users to view all their friends, making it easy to connect with and interact with people they’ve already added as connections.

### Key Features
- **List of Friends**: Displays a list of the user’s friends, showing each friend’s profile picture and name for easy identification.
- **Search by Name**: Users can search for a specific friend by their name to quickly find a connection in the list.
- **View Profile**: Tapping on a friend's profile allows users to view their details and connect further.
- **Real-Time Updates**: The friends list updates automatically to reflect any new friend additions or removals.

This fragment offers a centralized view of all the user’s friends, making it easy to stay connected and initiate conversations.

### Friends List Fragment Preview

<div style="display: flex; justify-content: space-around;">
    <img src="https://github.com/zivshamli/photosForHobbiesConnect/blob/main/%D7%AA%D7%9E%D7%95%D7%A0%D7%94%20%D7%A9%D7%9C%20WhatsApp%E2%80%8F%202024-11-06%20%D7%91%D7%A9%D7%A2%D7%94%2017.51.23_0fb3876b.jpg" alt="Friends List Fragment - Friend Overview" width="300" height="500"/>
    
</div>


## Chats List Fragment

The **Chats List Fragment** in HobbiesConnect provides users with a list of all their ongoing chats, sorted by the most recent message, making it easy to keep track of active conversations.

### Key Features
- **List of Conversations**: Displays a list of all active chats, showing the name of the friend or user, their profile picture, and the latest message.
- **Sorted by Latest Message**: The chat list is automatically sorted by the most recent message, so users can easily find and continue the most recent conversations.
- **Unread Messages**: Chats with unread messages are clearly marked, helping users identify new communications.
- **Real-Time Updates**: The chat list updates automatically as new messages are sent or received, ensuring that users always have the latest information.
- **Access Chat**: Tapping on a chat opens the conversation, allowing users to send and receive messages.

This fragment centralizes all chat interactions and ensures that users can always pick up where they left off in their most recent conversations.

### Chats List Fragment Preview

<div style="display: flex; justify-content: space-around;">
    <img src="https://github.com/zivshamli/photosForHobbiesConnect/blob/main/%D7%AA%D7%9E%D7%95%D7%A0%D7%94%20%D7%A9%D7%9C%20WhatsApp%E2%80%8F%202024-11-06%20%D7%91%D7%A9%D7%A2%D7%94%2017.51.23_ea09cb04.jpg" alt="Chats List Fragment - Active Chats" width="300" height="500"/>
   />
</div>


## Edit Profile Fragment

The **Edit Profile Fragment** in HobbiesConnect allows users to modify only their profile picture and hobbies. This fragment also includes a logout button, and once logged in, users do not need to re-enter their email and password on subsequent app launches, making the login experience seamless. Upon logging out, all cached data is cleared.

### Key Features
- **Update Profile Picture**: Users can upload a new profile picture from their gallery. Glide is used to load and display the image efficiently.
- **Select Hobbies**: Users can update their hobbies by selecting or changing up to 5 hobbies from a predefined list. If more than 5 hobbies are selected, the app will show a toast message prompting the user to choose up to 5 hobbies only.
- **Logout Button**: The fragment includes a "Logout" button that allows users to sign out of their account. Upon logout, all cached data, such as authentication tokens and profile information, is cleared, ensuring that the user must log in again when they open the app.
- **Save Changes**: After making the necessary changes (updating the profile picture and hobbies), the user can click the “Save” button to save the changes to their profile. A loading animation (such as Lottie) will appear while the data is being saved.
- **Real-Time Feedback**: If any required fields are missing or if the user selects more than 5 hobbies, the app provides real-time feedback with a toast message.

### Login Persistence
- After the initial login, users will not need to re-enter their email and password each time they open the app on the same device. The app will automatically authenticate the user based on the credentials stored during the first login session.
- **Logout**: When the user logs out, all cached data is cleared, including stored authentication tokens, ensuring the user's session is completely terminated.

This fragment enables users to easily update their profile picture and hobbies, while also offering a seamless login experience and the ability to securely log out.

### Edit Profile Fragment Preview

<div style="display: flex; justify-content: space-around;">
    <img src="https://github.com/zivshamli/photosForHobbiesConnect/blob/main/%D7%AA%D7%9E%D7%95%D7%A0%D7%94%20%D7%A9%D7%9C%20WhatsApp%E2%80%8F%202024-11-06%20%D7%91%D7%A9%D7%A2%D7%94%2017.51.24_3b09d411.jpg" alt="Edit Profile Fragment - Update Picture and Hobbies" width="300" height="500"/>
    <img src="https://github.com/zivshamli/photosForHobbiesConnect/blob/main/%D7%AA%D7%9E%D7%95%D7%A0%D7%94%20%D7%A9%D7%9C%20WhatsApp%E2%80%8F%202024-11-06%20%D7%91%D7%A9%D7%A2%D7%94%2017.51.24_98024e01.jpg" alt="Edit Profile Fragment - Logout Button" width="300" height="500"/>
</div>


## Chat Screen

The **Chat Screen** in HobbiesConnect allows users to engage in one-on-one messaging with their friends. It provides a simple and efficient chat interface where users can send and receive text messages in real-time.

### Key Features
- **Message Display**: Messages are displayed in a chat bubble format, with the user’s messages on the right and the friend’s messages on the left, creating a clear visual distinction between the two participants.
- **Send Text Messages**: Users can type and send text messages in real time using a simple text input field. The user can submit messages by pressing the send button (typically represented by a paper airplane icon).
- **Real-Time Updates**: New messages appear instantly as they are sent or received, making the conversation flow seamlessly without needing to refresh.
- **Scroll to Latest Message**: As new messages are received or sent, the chat will automatically scroll to the most recent message, ensuring users don’t need to manually scroll to follow the conversation.
- **Message Time Stamps**: Each message includes a timestamp to show when the message was sent, helping users track the conversation.
- **Back Button**: The chat screen includes a back button to allow users to return to the previous screen, such as the friends list or chat list.
  
### Chat Input
- Users can input their message into a text box at the bottom of the screen. A "Send" button, usually a paper airplane icon, allows them to submit the message.

### Chat Screen Preview

<div style="display: flex; justify-content: space-around;">
    <img src="https://github.com/zivshamli/photosForHobbiesConnect/blob/main/%D7%AA%D7%9E%D7%95%D7%A0%D7%94%20%D7%A9%D7%9C%20WhatsApp%E2%80%8F%202024-11-06%20%D7%91%D7%A9%D7%A2%D7%94%2017.51.23_d7db8b33.jpg" alt="Chat Screen - Conversation View" width="300" height="500"/>
</div>






















