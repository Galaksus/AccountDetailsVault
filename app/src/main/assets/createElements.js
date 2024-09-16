let _id;
let _category;
let _username; 
let _email; 
let _password; 

// Function to create and append outer elements
function createOuterElements(data) {
  console.log(data);

  data = parseJsonData(data);

  // Create the div with class 'settings-pane'
  const settingsPane = document.createElement("div");
  settingsPane.classList.add("outer-credentials-pane");
  settingsPane.id = data.category_name; // Make the ID of the element same as the category name

  // Create the h3 element
  const heading = document.createElement("h3");
  heading.textContent = /*data.categoryId + ": " +*/ data.category_name;

  // Append h3 to div
  settingsPane.appendChild(heading);

  // Append the div to the body 
  document.body.appendChild(settingsPane);
}


function createInnerElements(data) {
  console.log(data);

  data = parseJsonData(data);

  // Ensure the parsed data and target div exists
  if (data && data.category_name) {
      // Find the target div by category_name
      const targetDiv = document.getElementById(data.category_name);

      if (targetDiv) {
          // Create the parent div element for inner credentials pane
          const div = document.createElement('div');
          div.className = 'inner-credentials-pane';

          // Create and append the details-section div
          const detailsSection = document.createElement('div');
          detailsSection.className = 'details-section';

          // Create and append the Username label
          const idLabel = document.createElement('p');
          idLabel.innerHTML = `<strong>Id:</strong> ${data.id || 'N/A'}`;
          detailsSection.appendChild(idLabel);

          // Create and append the Email label
          const emailLabel = document.createElement('p');
          emailLabel.innerHTML = `<strong>Email:</strong> ${data.email || 'N/A'}`;
          detailsSection.appendChild(emailLabel);

          // Create and append the Username label
          const usernameLabel = document.createElement('p');
          usernameLabel.innerHTML = `<strong>Username:</strong> ${data.username || 'N/A'}`;
          detailsSection.appendChild(usernameLabel);

          // Create and append the Password label
          const passwordLabel = document.createElement('p');
          passwordLabel.innerHTML = `<strong>Password:</strong> ${data.password || 'N/A'}`;
          detailsSection.appendChild(passwordLabel);

          // Append the details-section to the parent div
          div.appendChild(detailsSection);

          // Create and append the edit-credentials img
          const editIcon = document.createElement('img');
          editIcon.id = 'testi';
          editIcon.className = 'edit-credentials';
          editIcon.src = 'icons/new-black.svg';
          editIcon.alt = 'Edit';
          editIcon.onclick = function() {
            // Find the closest parent div with the class 'outer-credentials-pane'
            const outerElement = this.closest('.outer-credentials-pane');
            
            // Get the category_name from the outerElement's ID
            if (outerElement) {
              _category = outerElement.id;
                console.log('Category Name:', _category);
                
                // Now call your showPopup function with the category name
                showPopup('Edit', this, _category);
            } else {
                console.error('Outer element with class "outer-credentials-pane" not found.');
            }
        };
          div.appendChild(editIcon);

          // Append the created div to the target div
          targetDiv.appendChild(div);
      } else {
          console.error(`Div with ID ${data.category_name} not found.`);
      }
  } else {
      console.error("Invalid data or category_name not found.");
  }
}


function parseJsonData(jsonString) {
    console.log("json stringi", jsonString);
    try {
      const data = JSON.parse(jsonString);
  
      // You can return the parsed object or handle it as needed
      return data;
    } catch (error) {
      console.error("Error parsing JSON:", error);
      return null;
    }
  }


  function showPopup(id, id2) {
    console.log(`showPopup called with id: ${id} and id2:`, id2);
    
    var popup = document.getElementById(id);
  
    if (popup.id == 'Edit') {
      var innerPane = id2.closest(".inner-credentials-pane");
      // Select all <p> elements within the container
      var paragraphs = innerPane.querySelectorAll("p");
  
      // Loop through all <p> elements
      paragraphs.forEach(function (paragraph) {
        var strongText = paragraph.querySelector("strong");
        if (strongText) {
          var key = strongText.textContent.replace(":", "").trim(); // Get text and remove ':'
          var value = paragraph.textContent
            .replace(strongText.textContent, "")
            .trim(); // Get text after <strong>
  
          // Store the result in the corresponding global variable
          if (key == "Id") {
            _id = value;
          }
          /*else if (key === "category") {
            _category = value;
          }*/
          else if (key === "Username") {
            _username = value;
          } else if (key === "Email") {
            _email = value;
          } else if (key === "Password") {
            _password = value;
          }
        }
      });

      var outerPane = id2.closest(".inner-credentials-pane");

      console.log("_id:", _id);
      console.log("_username:", _username);
      console.log("_email:", _email); 
      console.log("_password:", _password); 
    }
    // Toggle popup visibility
    popup.style.display = popup.style.display === "block" ? "none" : "block";
  }
  

 const updateButton = document.getElementById('update-button'); 
 const deleteButton = document.getElementById('delete-button'); 


updateButton.addEventListener('click', () => {
  let success = Android.updateRowById(_id);
  if (success) {
    Android.showToast("Row has beed updated");
  }
  else {
    Android.showToast("Failed to update row");
  }
});

deleteButton.addEventListener('click', () => {

  let success = Android.deleteRow(parseInt(_id, 10), _category);
  if (success) {
    Android.showToast("Row has beed deleted");
    // Clear all elements and reload UI
    document.body.innerHTML = '';
    window.location.reload();
  }
  else {
    Android.showToast("Failed to delete row");
  }
});


// Function to close the dialog
function closeDialog(element) {
  element.style.display = "none";
}

function copyTextToClipboard(element) {
  let text;
  
  if (element.id == 'copy-username') {
    console.log(_username);
    text = _username;
  }
  else if (element.id == 'copy-email') {
    console.log(_email);
    text = _email;

  }
  else if (element.id == 'copy-password') {
    console.log(_password);
    text = _password;

  }

  navigator.clipboard.writeText(text).then(() => {
      console.log('Text copied to clipboard successfully!');
  }).catch(err => {
      console.error('Failed to copy text: ', err);
      console.error('Failed to copy text.');
  });

}

// Get references to elements
const plusIcon = document.querySelector('.plus-icon'); 
const dialog = document.getElementById('add-new-dialog');
const cancelBtn = document.getElementById('cancelBtn');
const submitBtn = document.getElementById('submitBtn');
const dialogContent = document.querySelector('.add-new-dialog-content');
const categoryInput = document.getElementById('category');
const usernameInput = document.getElementById('username');
const emailInput = document.getElementById('email');
const passwordInput = document.getElementById('password');
const errorMessageContainer = document.createElement('div'); // Create error message container
// Add error message container at the end of dialogContent (once)
dialogContent.appendChild(errorMessageContainer);

// Function to hide error message
function hideErrorMessage() {
    errorMessageContainer.innerHTML = '';  // Clear the error message
}

// Show or hide dialog when plus icon is clicked
plusIcon.addEventListener('click', () => {
    dialog.style.display = dialog.style.display === "block" ? "none" : "block";
    hideErrorMessage(); // Hide error message when dialog is toggled
});

// Hide dialog and error message when cancel button is clicked
cancelBtn.addEventListener('click', () => {
    dialog.style.display = 'none';
    hideErrorMessage();
});

// Handle form submission when submit button is clicked
submitBtn.addEventListener('click', () => {
    const category = categoryInput.value;
    const username = usernameInput.value;
    const email = emailInput.value;
    const password = passwordInput.value;

    // Validation logic: Category, Password, and either Username or Email must be filled
    if (category && password && (username || email)) {
        // Log data to the console
        console.log(`Category: ${category}`);
        console.log(`Username: ${username}`);
        console.log(`Email: ${email}`);
        console.log(`Password: ${password}`);

        // Clear the inputs
        categoryInput.value = '';
        usernameInput.value = '';
        emailInput.value = '';
        passwordInput.value = '';

        // Clear the error message (if any)
        hideErrorMessage();

        // Close the dialog
        dialog.style.display = 'none';

        let dbUpdatedSuccessfully = Android.addCredentialsToDatabase(category, username, email, password);
        if (dbUpdatedSuccessfully) {
          Android.showToast("New account added to database.")

          // Clear all elements and reload UI
          document.body.innerHTML = '';
          window.location.reload();

        }
        else {
          Android.showToast("Failed to add new account to database.")
        }

    } else {
        // Display the error message in the container
        errorMessageContainer.innerHTML = '<p style="color: red;">Please fill out Category, Password, and either Username or Email.</p>';
    }
});

// Hide error message when any input is focused
categoryInput.addEventListener('focus', hideErrorMessage);
usernameInput.addEventListener('focus', hideErrorMessage);
emailInput.addEventListener('focus', hideErrorMessage);
passwordInput.addEventListener('focus', hideErrorMessage);




function OKClickOnAreYouSurePrompt() {
  /*
   * Performs the delete operation if OK is cliced on the Are you sure propmpt
   */
  AreYouSureDialog.style.display = "none";
  // Delete the row from database
  window.Android.deleteRowFromDb(currentIndex);
  refreshSelectElement(true);

  // Send a toast to Android about it
  window.Android.toastMessageFromJS("Route deleted successfully");
}

const AreYouSureDialog = document.getElementById("are-you-sure-dialog");




deleteButton.addEventListener('click', function() {
  /*
  * Reveals the "are you sure" dialog
  */
    AreYouSureDialog.style.display = "block";
});