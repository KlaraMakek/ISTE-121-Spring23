# Lab and Homework Assignments

This repository contains my solutions for various lab and homework assignments by professor Alan Mutka, but were created by Peter Lutz and David Patric's "ISTE-121 - Computational Problem Solving in the Information Domain II" course at RIT Golisano College of Computing and Information Sciences. The assignment descriptions and materials were provided by Peter Lutz and David Patric, and RIT.

## Course Information

- Course: ISTE-121 - Computational Problem Solving in the Information Domain II
- Assignment created by: Peter Lutz and David Patric
- Professor: Alan Mutka
- Solutions: Klara Makek
- College/University: RIT Golisano College of Computing and Information Sciences
- Semester/Year: Spring 2023

## Assignment Descriptions

The assignment descriptions and materials included in this repository were created and provided by Peter Lutz, David Patric, and RIT. They hold the intellectual property rights for these materials.

## Repository Structure

Each assignment is contained in its own folder, labeled with the assignment number or name. Inside each folder, you'll find my solution along with the original assignment description provided by Peter Lutz, David Patric, and RIT.

## Usage and Licensing

- The assignment materials and descriptions are the intellectual property of Peter Lutz, David Patric, and RIT.
- My solutions to the assignments are provided here for reference and educational purposes.
- If you intend to use, modify, or distribute these materials, please ensure that you comply with the terms set by Peter Lutz, David Patric, and RIT.
- Please do not use my solutions for academic dishonesty, and make sure to follow your institution's guidelines on academic integrity.

## Contact Information

If you have any questions about the solutions or the materials provided, you can reach me at:
- GitHub: https://github.com/KlaraMakek

Please respect the intellectual property rights of the course materials and follow ethical practices when using and sharing these resources.

## Finish grade

GUI Appearance

Clock: (10%)

- Clock at top in different font and size than normal Label, HTML/font-color (4/4)
- - Clock shows time in specified format (ex: Tue, 26 Jan 2021 14:23:27) (4/4)
Clock smoothly changes time every second (2/2)

Rainbow: (10%)

- Rainbow of 7 colors, ROYGBIV, appear in the middle (3/3)
- Rainbow smoothly rotates colors up the GUI, colors don’t skip (4/4)
- Rainbow moves every ½ second (3/3)

File reading: (25%)

- Two labeled progress bars, show “indeterminate” on startup, with
- “Opening Words”, and “Opening Unabridged” (or similar wording) (5/5)
- Progress bar uses 0%-100% (4/4)
- Word changes ##% faster than Unabridged ##% (4/4)
- Word progress bar stops at 100% for two seconds, before changing (4/4)
 
At the same time:

- Progress bars stop with % of file read displayed (3/3)
- Rainbow colors stop progressing, possibly after a delay (5/5)
- Help à About works while File reading is advancing the progress bars (5/5)


CODE

Clock:

- Uses java.util.Timer class  (10/10)

Rainbow:

- Uses java.util.Timer class (5/5)
- Colors are defined using Color (3/3)
- Waits 2 seconds before starting cycling colors(2/2)
- Code is simple, showing programmer understands java.util.Timer (6/6)

Reading files:

- Uses ONE inner class of either Thread or Runnable class to create objects (5/5)
- One per file being read, which directly correlates to the progress bar. (4/4)
- Read file with BufferedReader or Scanner, a line at a time (one char at a time is
invalid) (3/3)
- Concatenate the words to a String variable (no StringBuffer/StringBuilder)
- Adjusts read in length of line, so it does not stop at 83% or 91% (not -2)  (3/3)
 
- Overall layout of code, logic, and documentation (4/4)