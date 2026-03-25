# Snow_Co.
Team members- Zoha, Suha, Seline, Zainab, Ilakkiyan

# Library Management System – MVP

## Basic Setup Instructions

Follow these steps to run the application's Minimum Viable Product (MVP).

## 1. Requirements

Make sure the following are installed on your system:

- **Java JDK 17 or later**
- **Git**
- **Gson Library** (`gson-2.10.1.jar` included in the `lib` folder)

## 2. Developer Onboarding: How to Compile, Test, and Run

Welcome to the team! To make onboarding as seamless as possible, we have automated the build and execution process. You do not need to manually configure the Java classpath or compile individual files.

### For Windows Users
1. Open your Command Prompt or PowerShell and navigate to the root directory of the project (`Snow_Co`).
2. Execute the batch script to compile and launch:
   `.\run.bat`

### For Mac / Linux Users
1. Open your terminal and navigate to the root directory of the project (`Snow_Co`).
2. Grant execution permissions to the shell script (you only need to do this once):
   `chmod +x run.sh`
3. Execute the script to compile and launch:
   `./run.sh`

### What these scripts do automatically:
When you run either script, the system will perform the following steps to ensure a clean build:
* **Clean:** Deletes any outdated `.class` files in the `target/classes` directory.
* **Link:** Automatically connects the necessary dependencies located in `UI/lib/` (`gson.jar` and `junit.jar`).
* **Compile:** Compiles all source code and test files from the `UI/src/` directory into the `target` folder.
* **Launch:** Starts the Java Virtual Machine and launches the `LoginWindow` application.

---

## 3. Run the Application

If the build is successful, the program will automatically open the Login Window.

Log in using either of the following test accounts:

### User account → Opens the User Dashboard:
- Username is user.
- User password is password.
  
### Admin account → Opens the Admin Dashboard:
- Username is admin.
- Admin password is password.

From the dashboards, you can access the available  features, such as viewing books or the search bar.
