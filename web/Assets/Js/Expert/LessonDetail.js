// function to choose the fields to appear int the page 
async function displayFields() {

    await loadPreloader();
    // video-input, content-input, quiz-input 
    var video = document.getElementById('video-input');
    var content = document.getElementById('content-input');
    var quiz = document.getElementById('quiz-input');

    var topic = document.getElementById('topic_select');
    var topic_input = document.getElementById('topic-input');

    // get type-select
    var type = document.getElementById('type-select').value;

    // input fields
    var lesson_id = document.getElementById('lesson_id-input');
    var subject_id = document.getElementById('subject_id-input');
    var video_url = document.getElementById('video-link');


    // if type is topic, hide all the above fields
    if (type == '1') {
        video.style.display = 'none';
        content.style.display = 'none';
        quiz.style.display = 'none';
        topic.style.display = 'none';
        // topic_input.style.display = 'block'

        // set required
        video_url.required = false;
        lesson_id.required = false;
        subject_id.required = false;
    }
    // else if type is quiz, hide video and content fields
    else if (type == '3') {
        video.style.display = 'none';
        content.style.display = 'none';
        quiz.style.display = 'flex';
        // topic_input.style.display = 'none';
        topic.style.display = 'flex';

        // set required
        video_url.required = false;
        lesson_id.required = false;
        subject_id.required = false;
    }
    // else show all
    else {
        video.style.display = 'block';
        content.style.display = 'block';
        quiz.style.display = 'none';
        // topic_input.style.display = 'none';
        topic.style.display = 'flex';
    }

    hidePreloader();

}

function loadStatus() {
    // get the status select
    var status = document.getElementById('status-select');
    // value = 0 then style color red
    if (status.value == '0') {
        status.style.color = 'red';
    }
    // value = 1 then style color green
    else {
        status.style.color = 'green';
    }

    // problem: color of the arrow also changed
    // solution: change the color of the arrow to black
    // get the arrow
}

function loadQuizDetail() {
    // get the selected option
    var selected_option = document.getElementById('quiz-select').options[document.getElementById('quiz-select').selectedIndex];
    // get data of selected option in data- attribute
    var quiz_name = selected_option.getAttribute('data-name');
    var level = selected_option.getAttribute('data-level');
    var duration = selected_option.getAttribute('data-duration');
    var passRate = selected_option.getAttribute('data-pass-rate');
    var numQuestions = selected_option.getAttribute('data-question-number');

    // level 1 means easy, 2 means medium, 3 means hard
    switch (level) {
        case '1':
            level = 'Easy';
            break;
        case '2':
            level = 'Medium';
            break;
        case '3':
            level = 'Hard';
            break;
        default:
            level = 'Easy';
    }

    // set the values in the fields
    document.getElementById('quiz_name').innerHTML = quiz_name;
    document.getElementById('quiz_level').innerHTML = level;
    document.getElementById('duration').innerHTML = duration + ' mins';
    document.getElementById('pass_rate').innerHTML = passRate + '%';
    document.getElementById('question_number').innerHTML = numQuestions;

    console.log(quiz_name, level, duration, passRate, numQuestions);
}

// load preloader when page is loading
function loadPreloader() {
    var preloader = document.getElementById('pre-loader');
    preloader.style.display = 'flex';
}

// function to hide preloader
function hidePreloader() {
    var preloader = document.getElementById('pre-loader');
    preloader.style.display = 'none';
}

function processAction() {
    // get form by id detail_form
    var form = document.getElementById('detail_form');

    // get fields
    var action = document.getElementById('action-input').value;
    var lesson_id = document.getElementById('lesson_id-input').value;

    switch (action) {
        case 'view':
            // redirect to edit page
            window.location.href = 'lesson-detail?action=edit&lesson_id=' + lesson_id;
            break;
        default:
            form.submit();
    }
}

function cancelAction() {

    // get fields
    var action = document.getElementById('action-input').value;
    var lesson_id = document.getElementById('lesson_id-input').value;
    var subject_id = document.getElementById('subject_id-input').value;

    switch (action) {
        case 'view':
            // redirect to lessons list
            window.location.href = 'lessons?subject_id=' + subject_id;
            break;
        case 'add':
            // redirect to lessons list
            window.location.href = 'lessons?subject_id=' + subject_id;
            break;
        default:
            // redirect to view page
            window.location.href = 'lesson-detail?action=view&lesson_id=' + lesson_id;
            break;
    }
}

function checkVideoLink() {
    // if lesson type is not 2 return
    if (document.getElementById('type-select').value != '2') {
        return;
    }

    // get video link
    var video_link = document.getElementById('video-link').value;
    // get iframe
    var video_frame = document.getElementById('video-iframe');

    // try to request the video link, if fail then video_frame src = ''
    try {
        // https://www.youtube.com/embed/-rsWIApDc8g

        // get video id
        var video_id = video_link.split('embed/')[1];

        // url to check if video is valid
        var url = 'https://www.youtube.com/oembed?url=https://www.youtube.com/watch?v=' + video_id;

        // request the url
        fetch(url)
            .then(response => {
                // if response is not ok then video_frame src = ''
                if (!response.ok) {
                    video_frame.src = '';
                }
                // else video_frame src = video_link
                else {
                    video_frame.src = video_link;
                    document.getElementById('video-link').setCustomValidity('');
                }
            })
            .catch(error => {
                video_frame.src = '';
            });

    } catch (error) {
        video_frame.src = '';
    }

}

async function validateVideoLink(e) {

    // if lesson type is not 2 return
    if (document.getElementById('type-select').value != '2') {
        return;
    }

    if (e)
        e.preventDefault(); // Prevent form submission until validation is complete

    var videoLink = document.getElementById('video-link');
    var videoFrame = document.getElementById('video-iframe');

    try {
        console.log("Validation is running");

        // Extract video ID from the link
        var videoId = videoLink.value.split('embed/')[1];

        // Construct the URL to check if the video is valid
        var url = 'https://www.youtube.com/oembed?url=https://www.youtube.com/watch?v=' + videoId;

        // Fetch the URL to validate the video
        const response = await fetch(url);

        if (!response.ok) {
            // Set custom validity message for invalid video link
            videoLink.setCustomValidity('Invalid video link');
        } else {
            // Set the video frame source to the video link
            videoFrame.src = videoLink.value;
            // Clear the custom validity message if video is valid
            videoLink.setCustomValidity('');
            // Optionally submit the form here if validation passes
            // e.target.submit();
        }
    } catch (error) {
        // Set custom validity message for fetch errors
        videoLink.setCustomValidity('Invalid video link');
    } finally {
        // Report the validity of the video link to the form
        videoLink.reportValidity();
    }

    if (e) {
        // submit the form
        var form = document.getElementById('detail_form');

        // Check if the form is valid
        if (form.checkValidity()) {
            // If all fields are valid, submit the form
            form.submit();
        } else {
            // log the validation messages

            // If the form is invalid, display the validation messages
            form.reportValidity();
        }
    }
}


// on page load
window.onload = async function () {
    // display fields
    await displayFields();
    checkVideoLink();
    loadQuizDetail();
    loadStatus();
    
    // event listener for form
    var form = document.getElementById('detail_form');
    form.addEventListener('input', () =>{
        console.log("Form input changed");
        // display the save button
        document.getElementById('saveButton').style.display = 'block';
    });
}