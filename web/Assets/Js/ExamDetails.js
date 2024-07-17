function showPopup() {
    const message = "<h3 style='text-align: center;'>Start Simulation Exam</h3> <br/> Would you like to start a new attempt now? <br/> Click 'Yes' to begin or 'Cancel' to cancel.";
    
    document.getElementById('popupMessage').innerHTML = message;
    document.getElementById('submit-overlay').style.display = 'block';
    document.getElementById('submit-popup').style.display = 'block';
}

function hidePopup() {
    document.getElementById('submit-overlay').style.display = 'none';
    document.getElementById('submit-popup').style.display = 'none';
}

function startExam(quiz_id){
    // window redirect to link quiz-handle?action=start&quiz_id=quiz_id
    window.location.href = 'quiz-handle?action=start&quiz_id=' + quiz_id;
}

function redirectToSimulation() {
    window.location.href = 'simulation-exam';
}

