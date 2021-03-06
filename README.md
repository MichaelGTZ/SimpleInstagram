# Project 4 - *SimpleInstagram*

**SimpleInstagram** is a photo sharing app using Parse as its backend.

Time spent: **22** hours spent in total

## User Stories

The following **required** functionality is completed:

- [X] User sees app icon in home screen.
- [X] User can sign up to create a new account using Parse authentication
- [X] User can log in and log out of his or her account
- [X] The current signed in user is persisted across app restarts
- [X] User can take a photo, add a caption, and post it to "Instagram"
- [X] User can view the last 20 posts submitted to "Instagram"
- [X] User can pull to refresh the last 20 posts submitted to "Instagram"
- [X] User can tap a post to view post details, including timestamp and caption.

The following **stretch** features are implemented:

- [X] Style the login page to look like the real Instagram login page.
- [X] Style the feed to look like the real Instagram feed.
- [ ] User should switch between different tabs - viewing all posts (feed view), capture (camera and photo gallery view) and profile tabs (posts made) using fragments and a Bottom Navigation View.
    - All except photo gallery view are implemented.
- [X] User can load more posts once he or she reaches the bottom of the feed using endless scrolling.
- [X] Show the username and creation time for each post
- [ ] After the user submits a new post, show an indeterminate progress bar while the post is being uploaded to Parse
- User Profiles:
  - [X] Allow the logged in user to add a profile photo
  - [ ] Display the profile photo with each post
  - [ ] Tapping on a post's username or profile photo goes to that user's profile page
  - [ ] User Profile shows posts in a grid view
- [ ] User can comment on a post and see all comments for each post in the post details screen.
- [ ] User can like a post and see number of likes for each post in the post details screen.

The following **additional** features are implemented:

- [ ] List anything else that you can get done to improve the app functionality!

Please list two areas of the assignment you'd like to **discuss further with your peers** during the next class (examples include better ways to implement something, how to extend your app in certain ways, etc):

1. How to handle the comments and likes in Parse as well as programatically to make sure that the home screen and detailed post screen show correct information
2. How to use the actual instagram API to make an app similar to SimpleTweet

## Video Walkthrough

Here's a walkthrough of implemented user stories:

[Demo Link](screenshots/SimpleIG_Demo.gif)

[Demo of adding sign up ability from required features](screenshots/SimpleIG_SignupDemo.gif)

[Demo of adding profile photo stretch story](screenshots/SimpleIG_ProfilePicDemo.gif)

GIF created with [Kap](https://getkap.co/).

## Credits

List an 3rd party libraries, icons, graphics, or other assets you used in your app.

- [Android Async Http Client](http://loopj.com/android-async-http/) - networking library
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android


## Notes

Describe any challenges encountered while building the app.

Whilst trying to implement a like count, I was unable to figure out how to change the icon between its filled in and outlined form based on if a user has clicked it or not. Without this, I wasn't able to properly implement the likes feature, as I wasn't able to show accurate like counts and add/remove from a posts like count. I believe I could have gotten this to work with more time spent studying Parse workflow.

## License

    Copyright [2020] [Michael Gutierrez]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
