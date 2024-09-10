# Card-Matching Game App

## Overview

This card-matching game challenges users to match pairs of cards from a randomly shuffled 4x4 grid. The user can start the game after entering and verifying their email, and their performance is recorded based on how fast they complete the game. A leaderboard system tracks and displays scores for different users, using Firebase for remote data storage.

## Table of Contents
- [Features](https://github.com/ZJShown/card_matching?tab=readme-ov-file#features)
- [Screens](https://github.com/ZJShown/card_matching?tab=readme-ov-file#screens)
- [Technical Details](https://github.com/ZJShown/card_matching?tab=readme-ov-file#technical-details)
- [Installation](https://github.com/ZJShown/card_matching?tab=readme-ov-file#installation)
- [Future Improvements](https://github.com/ZJShown/card_matching?tab=readme-ov-file#future-improvements)
- [Screenshots](https://github.com/ZJShown/card_matching?tab=readme-ov-file#screenshots)

## Features

- **Grid Layout**: The game board consists of a 4x4 grid where each cell contains a hidden card. Cards are shuffled randomly at the start of each game.
- **Card Matching**: Users reveal cards by tapping. If two revealed cards match, they stay revealed; otherwise, they flip back.
- **Timer and Score**: The game tracks how long the user takes to complete all matches, and this time serves as their score.
- **Progress Bar**: A dynamic progress bar tracks the player's progress. As matches are found, the bar fills up and changes color from red to yellow to green.
- **Email Verification**: Users enter their email address to start the game. A verification code is sent to the entered email, which must be entered to proceed.
- **Leaderboard**: The leaderboard displays player rankings based on completion time. The leaderboard is stored in Firebase and shows players’ ranks, usernames, and scores. The current user's score is highlighted.
- **Local Data**: The app persists the user's email and score locally, so they do not need to verify their email again unless they change it.
- **Fake Ads**: A fake ad is displayed during gameplay to simulate the use of ads in the app.
- **Replay Option**: After completing the game, the user has the option to play again or view the leaderboard.

## Screens

1. **Starting Screen**:
   - Allows users to enter their email.
   - Includes a "Start Game" button, and a "Send Code" button which sends a verification code to the given email.
   - Fake ad displayed at the bottom of the screen.

2. **Game Screen**:
   - Displays a 4x4 grid of hidden cards.
   - Timer counting up from 0 seconds, used for scoring.
   - Progress bar that fills up as the user matches pairs of cards, changing color as the user progresses.
   - "You Won!" screen displayed after all matches are found, showing the user's score and a button to view the leaderboard.

3. **Leaderboard Screen**:
   - Displays a table with all player ranks, usernames, and scores in ascending order of time.
   - The row of the current user is highlighted (if they are on the leaderboard.
   - A "Play Again" button that returns to the starting screen.

## Technical Details

- **Model-View-Controller (MVC)** architecture is used for managing the game logic, views, and data handling.
- **Local Data Persistence**: The app saves the user's email and last score locally, so users don’t need to re-enter email verification if they want to replay.
- **Firebase Integration**: Used to store and retrieve leaderboard data.
- **Customizable Game Settings**: The game dynamically shuffles cards and keeps track of user progress.
- **GUI Components**: 
   - Timer display counting up from 0, recording the user's score.
   - Progress bar indicating progress in the game, changing color based on completion.

## Installation

1. Clone the repository.
2. Open the project in Android Studio.
3. Connect the app to Firebase by following Firebase's setup guide for Android projects.
4. Build and run the app on your Android device or emulator.

## Future Improvements

- Add more levels of difficulty with larger grids.
- Implement animations for card flipping.
- Add sound effects and haptic feedback for a more engaging experience.

## Screenshots

<h3>Starting Screen with Advertisement and Email Verification</h3>

<img width="353" alt="Screenshot 2024-09-09 at 11 05 53 AM" src="https://github.com/user-attachments/assets/1e79a857-513b-489b-80b6-7eb445fdebaa">

<h3>Game Play with Timer and Progress Bar</h3>

<img width="361" alt="Screenshot 2024-09-09 at 11 04 20 AM" src="https://github.com/user-attachments/assets/35b13758-7456-4eb9-af38-709a51410ce0">

<h3>Top 15 Leaderboard Ranked by Time</h3>

<img width="410" alt="Screenshot 2024-09-09 at 11 04 49 AM" src="https://github.com/user-attachments/assets/50e99a59-9551-4b91-b19d-0771bbee6914">

<h3>Restart Screen After Clicking "Play Again"</h3>

<img width="353" alt="Screenshot 2024-09-09 at 11 03 07 AM" src="https://github.com/user-attachments/assets/bd5dd2a5-6b22-40c8-9fe7-1f1754a2a5b7">
