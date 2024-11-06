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

Once all information is filled out, users tap the **Save** button to complete registration. A loading indicator appears while the app saves the data and then transitions to the main navigation screen.

### Sign Up Screen Preview

<div style="display: flex; justify-content: space-around;">
    <img src="https://github.com/zivshamli/photosForHobbiesConnect/blob/main/%D7%AA%D7%9E%D7%95%D7%A0%D7%94%20%D7%A9%D7%9C%20WhatsApp%E2%80%8F%202024-11-06%20%D7%91%D7%A9%D7%A2%D7%94%2016.57.42_0a38e727.jpg" alt="Sign Up Screen - Step 1" width="300" height="500"/>
    <img src="[https://github.com/zivshamli/photosForHobbiesConnect/blob/main/SignUpScreen2.jpg](https://github.com/zivshamli/photosForHobbiesConnect/blob/main/%D7%AA%D7%9E%D7%95%D7%A0%D7%94%20%D7%A9%D7%9C%20WhatsApp%E2%80%8F%202024-11-06%20%D7%91%D7%A9%D7%A2%D7%94%2016.57.42_5364f3a3.jpg)" alt="Sign Up Screen - Step 2" width="300" height="500"/>
</div>











