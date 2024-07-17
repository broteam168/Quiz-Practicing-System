document.addEventListener('DOMContentLoaded', function () {

    document.getElementById('thumbnailUpload').addEventListener('change', function (event) {
        const file = event.target.files[0];
        const preview = document.getElementById('thumbnailPreview');
        const allowedExtensions = ['jpg', 'jpeg', 'png'];
        const extension = file.name.split('.').pop().toLowerCase();
        const messageProfile = document.getElementById('message-course');
        messageProfile.innerText = '';

        // Check if the image is jpg or png type or not
        if (!allowedExtensions.includes(extension)) {
            messageProfile.style.color = 'red';
            messageProfile.innerText = "Only JPG and PNG files are allowed.";
            return;
        }

        // Check if the image size exceeds 2MB
        const maxSize = 2 * 1024 * 1024;
        if (file.size > maxSize) {
            messageProfile.style.color = 'red';
            messageProfile.innerText = "The file size should not exceed 2MB.";
            return;
        }
        // Check if user changed thumbnail
        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                preview.src = e.target.result;
            }
            reader.readAsDataURL(file);
        } else {
            preview.src = 'Assets/Styles/Images/User/default-avatar.jpg';
        }
    });

    //click at tab overview for default
    document.getElementsByClassName("course-details-tab-tablinks")[0].click();

    const subjectInput = document.getElementById('subjectName');
    const defaultValue = subjectInput.value;

    subjectInput.addEventListener('blur', function () {
        const value = subjectInput.value.trim();
        const isValid = /[a-zA-Z]/.test(value) && value.length > 0 && value.length <= 50;
        //check validation for subject name
        if (!isValid) {
            subjectInput.value = defaultValue;
        }
    });

    const descriptionTextarea = document.getElementById('description');
    const defaultDescription = descriptionTextarea.value.trim();

    descriptionTextarea.addEventListener('blur', function () {
        const value = descriptionTextarea.value.trim();
        const isValid = value.length > 0 && value.length <= 1000;
        //check validation for description
        if (!isValid) {
            descriptionTextarea.value = defaultDescription;
        }
    });

    const taglineInput = document.getElementById('tagline');
    const defaultTagline = taglineInput.value.trim();

    taglineInput.addEventListener('blur', function () {
        const value = taglineInput.value.trim();
        const isValid = value.length > 0 && value.length <= 70;
        //check validation for tag line
        if (!isValid) {
            taglineInput.value = defaultTagline;
        }
    });

});

function openTab(evt, tabName) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("course-details-tab-content");
    //hide other tabs
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    //hide active tag for tab
    tablinks = document.getElementsByClassName("course-details-tab-tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" tabactive", "");
    }
    document.getElementById(tabName).style.display = "block";
    evt.currentTarget.className += " tabactive";
}

function openNotificationPopup(expertId) {
    document.getElementById("unassign-popup").style.display = "block";
    document.getElementById("unassign-id").value = expertId;
}

function assignPopup() {
    //check if the addExpert has expert id to assign
    if (document.getElementById("addExpert").value !== "") {
        document.getElementById("assign-popup").style.display = "block";
        document.getElementById("assign-id").value = document.getElementById("addExpert").value;
    }
}

function submitCourse() {
    var form = document.getElementById("courseDetails");
    //check valid for form
    if (form.checkValidity()) {
        form.submit();
    } else {
        console.log(form.validationMessage);
        form.reportValidity();
    }
}

function closePopup() {
    document.getElementById("unassign-popup").style.display = "none";
    document.getElementById("assign-popup").style.display = "none";
    document.getElementById("update-popup").style.display = "none";
}

function unassignExpert() {
    var userId = document.getElementById('unassign-id').value;
    var subjectId = document.getElementById('subjectId').value;

    $.ajax({
        url: 'course-details',
        method: 'POST',
        data: {
            expertId: userId,
            subjectId: subjectId,
            act: 'unassign'
        },
        success: function (data) {
            if (data.success) {
                var expertElement = document.getElementById(`expert-${userId}`);
                //check if the expert is exists
                if (expertElement) {
                    expertElement.parentNode.removeChild(expertElement);
                }
                var addExpertSelect = document.getElementById('addExpert');
                var option = document.createElement('option');
                option.value = data.expert.user_id; // Assuming data contains expert details
                option.text = `${data.expert.full_name} - ${data.expert.email} - ${data.expert.mobile}`;
                addExpertSelect.appendChild(option);
                closePopup();
            } else {
                console.log("Unassign failed. Server response:", data.message);
            }
        },
        error: function (error) {
            console.log("Unassign failed. Error:", error);
        }
    });
}

function assignExpert() {
    var selectElement = document.getElementById('addExpert');
    var selectedUserId = selectElement.value;
    var subjectId = document.getElementById('subjectId').value;
    document.getElementById("assign-popup").style.display = "none";

    //check if there is any selected user
    if (selectedUserId) {
        $.ajax({
            url: 'course-details',
            method: 'POST',
            data: {
                expertId: selectedUserId,
                subjectId: subjectId,
                act: 'assign'
            },
            success: function (data) {
                // Remove the assigned expert from the select dropdown
                selectElement.remove(selectElement.selectedIndex);
                //Add new expert to assigned list
                var newExpert = data.expert;
                var expertListContainer = document.getElementById('expertListContainer');
                //check existence for expert list
                if (expertListContainer) {
                    var newExpertHtml =
                            '<div class="expert-item">' +
                            '<input type="hidden" value="' + newExpert.user_id + '" name="expertId">' +
                            '<div class="expert-member" id="expert-' + newExpert.user_id + '">' +
                            '<p>' + newExpert.full_name + '</p>' +
                            '<br/>' +
                            'Email: <strong>' + newExpert.email + '</strong> &ensp;' +
                            'Phone: <strong>' + newExpert.mobile + '</strong> &ensp;' +
                            '<button class="course-details-button-back unassign" onclick="openNotificationPopup(' + newExpert.user_id + ')">Unassign</button>' +
                            '</div>' +
                            '</div>';
                    expertListContainer.innerHTML += newExpertHtml;
                } else {
                    console.error('#expertListContainer element not found.');
                }
            },
            error: function (error) {
                console.log("Assign expert failed. Error:", error);
            }
        });
    } else {
        alert('Please select an expert.');
    }
}

function showAddSelection() {
    document.getElementById("unExpert").style.display = "block";
    document.getElementById("addExpert-butt").style.display = "none";
}

const form = document.getElementById('courseDetails');
const button = document.querySelector('.submit-butt');
const initialFormData = new FormData(form);

//check if input in form changed
function hasFormChanged() {
    const currentFormData = new FormData(form);
    for (const [key, value] of initialFormData.entries()) {
        if (currentFormData.get(key) !== value) {
            return true;
        }
    }
    return false;
}

//update button css if the form is changed
function updateButtonState() {
    if (hasFormChanged()) {
        button.classList.add('active');
        button.disabled = false;
    } else {
        button.classList.remove('active');
        button.disabled = true;
    }
}

// check all inputs, selects, checkboxes, and file uploads for changes
form.querySelectorAll('input, select, textarea').forEach(element => {
    element.addEventListener('input', updateButtonState);
    element.addEventListener('change', updateButtonState);
});

//if form changed, submit form
function submitCourse() {
    event.preventDefault();
    if (hasFormChanged()) {
        savePopup();
//        form.submit();
    } else {
        alert('No changes detected.');
    }
}

function savePopup() {
    document.getElementById("update-popup").style.display = "block";
}

function saveChanges() {
    document.getElementById("update-popup").style.display = "none";
    var messageProfile = document.getElementById('message-course');
    messageProfile.style.color = 'blue';
    messageProfile.innerText = "Update course successfully!";
    form.submit();
}
