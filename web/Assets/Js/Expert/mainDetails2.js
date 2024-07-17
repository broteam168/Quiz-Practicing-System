document.addEventListener('DOMContentLoaded', () => {

    let sidebar = document.querySelector(".marketing-sidebar");
    let closeBtn = document.querySelector("#marketing-btn");
    let main = document.querySelector(".main");

    closeBtn.addEventListener("click", () => {
        main.classList.toggle("main-open");
    });
    function handleSelectChange() {
        var selectedValue = $('#subject').val();
        $.ajax({
            /// handle information to request
            type: "GET",
            url: "question-details?action=dimension&subjectId=" + selectedValue,
            dataType: "json",
            success: function (response) {
                if (response.status === 'success') {
                    var subjectDimensions = response.subjectDimensions;
                    var output = '<option value=0>None</option>';
                    $.each(subjectDimensions, function (index, subjectDimension) {
                        output += '<option value=' + subjectDimension.dimension_id + '>' + subjectDimension.name + '</option>';
                    });
                    output += '';
                    $('#dimension').html(output);
                } else {

                }
            },
            error: function (xhr, status, error) {
                console.log("An error occurred: " + xhr.responseText);
            }
        });

        $.ajax({
            /// handle information to request
            type: "GET",
            url: "question-details?action=lesson&subjectId=" + selectedValue,
            dataType: "json",
            success: function (response) {
                if (response.status === 'success') {
                    var lessons = response.lessons;
                    var output = '<option value=0>None</option>';
                    $.each(lessons, function (index, lesson) {
                        output += '<option value=' + lesson.lesson_id + '>' + lesson.name + '</option>';
                    });
                    output += '';
                    $('#lesson').html(output);
                } else {

                }
            },
            error: function (xhr, status, error) {
                console.log("An error occurred: " + xhr.responseText);
            }
        });
    }

    $('#subject').change(handleSelectChange);





    var selectedValue = $('#subject').val();

    $.ajax({
        /// handle information to request
        type: "GET",
        url: "question-details?action=dimension&subjectId=" + selectedValue,
        dataType: "json",
        success: function (response) {
            if (response.status === 'success') {
                var subjectDimensions = response.subjectDimensions;
                var output = '<option value=0>None</option>';
                $.each(subjectDimensions, function (index, subjectDimension) {
                    output += '<option value=' + subjectDimension.dimension_id + '>' + subjectDimension.name + '</option>';
                });
                output += '';
                $('#dimension').html(output);
                var result = subjectDimensions.find(obj => {
                    return obj.dimension_id == jsAtt;
                });
                if (result === null || result === undefined)
                    $('#dimension').val(0);
                else
                    $('#dimension').val(jsAtt);
            } else {

            }
        },
        error: function (xhr, status, error) {
            console.log("An error occurred: " + xhr.responseText);
        }
    });

    $.ajax({
        /// handle information to request
        type: "GET",
        url: "question-details?action=lesson&subjectId=" + selectedValue,
        dataType: "json",
        success: function (response) {
            if (response.status === 'success') {
                var lessons = response.lessons;
                var output = '<option value=0>None</option>';
                $.each(lessons, function (index, lesson) {
                    output += '<option value=' + lesson.lesson_id + '>' + lesson.name + '</option>';
                });
                output += '';
                $('#lesson').html(output);
                var result = lessons.find(obj => {
                    return obj.lesson_id == jsAtt2;
                });
                if (result === null || result === undefined)
                    $('#lesson').val(0);
                else
                    $('#lesson').val(jsAtt2);
            } else {

            }
        },
        error: function (xhr, status, error) {
            console.log("An error occurred: " + xhr.responseText);
        }
    });

    $('#btnAddAnswer').on('click', function () {
        $('#addContent').css('display', 'flex');
        $('html, body').animate({
            scrollTop: $('#addContent').offset().top
        }, 1000);
    });


    $('#btnCancel').on('click', function () {
        $('#addContent').css('display', 'none');
    });
    if (scroll == 'end')
        $('html, body').animate({
            scrollTop: $('#btnAddAnswer').offset().top
        }, 1000);


    $('.deleteForm').on('submit', function (event) {
        if (!confirm('Are you sure you want to delete this?')) {
            event.preventDefault(); // Ngăn chặn form submit nếu người dùng chọn "Cancel"
        }
    });

    $('#btnUpload').on('click', function ()
    {

        $('#uploadfile').click();
    });
    $('#uploadfile').change(function (e) {
        if (e.target.files.length > 0) {
            var fileName = e.target.files[0].name;
            var extension = fileName.split('.').pop().toLowerCase();
            var allowedExtensions = ['mp3', 'mp4', 'jpg', 'png'];

            if (allowedExtensions.includes(extension)) {
                $('#link').val('/Assets/Media/Question/' + questionId + '/main.' + extension);
                $('#statusMessage').text("File ready");
            } else {
                $('#statusMessage').text("Invalid file type. Please select an mp3, mp4, jpg, or png file.");
            }

        }
    });
    $('#btnPreview').on('click', function ()
    {

        const fileInput = $('#uploadfile')[0];
        if (fileInput.files.length <= 0 && $('#link').val().includes('/Assets/Media/Question/'))
        {
            $('#statusMessage').css('color', 'red');
            $('#statusMessage').text("Please select a file");
            return;
        }
        const file = fileInput.files[0];
        var fileType;
        var extension;
        if (!file && $('#link').val().includes('/Assets/Media/Question/')) {
            $('#statusMessage').css('color', 'red');
            $('#statusMessage').text("No file selected");
            return;
        } else if ($('#link').val().includes('/Assets/Media/Question/')) {
            extension = file.name.split('.').pop().toLowerCase();
            fileType = file.type;
        }

        if ($('#link').val() == '/Assets/Media/Question/' + questionId + '/main.' + extension)
        {
            $('#statusMessage').css('color', 'grey');
            $('#statusMessage').text("");
            const previewContainer = document.getElementById('preview');
            previewContainer.innerHTML = '';
            if (fileType.startsWith('image')) {
                const img = document.createElement('img');
                img.src = URL.createObjectURL(file);
                img.style.maxWidth = '500px'; // Limit image size for preview
                img.onload = function () {
                    URL.revokeObjectURL(img.src); // Free memory when done
                };
                previewContainer.appendChild(img);
            } else if (fileType.startsWith('video')) {
                const video = document.createElement('video');
                video.src = URL.createObjectURL(file);
                video.controls = true;
                video.style.maxWidth = '500px';
                video.onload = function () {
                    URL.revokeObjectURL(video.src);
                };
                previewContainer.appendChild(video);
            } else if (fileType.startsWith('audio')) {
                const audio = document.createElement('audio');
                audio.src = URL.createObjectURL(file);
                audio.controls = true;
                audio.onload = function () {
                    URL.revokeObjectURL(audio.src);
                };
                previewContainer.appendChild(audio);
            } else {
                previewContainer.innerHTML = 'File type not supported for preview.';
            }
        } else
        {
            var url = $('#link').val();
            var previewContainer = $('#preview');
            previewContainer.empty(); // Clear previous content

            if (!url) {
                previewContainer.text('Please enter a valid URL.');
                return;
            }

            var fileExtension = url.split('.').pop().toLowerCase();

            if (['jpg', 'jpeg', 'png', 'gif'].includes(fileExtension)) {
                var img = $('<img>').attr('src', url).css('maxWidth', '500px');
                previewContainer.append(img);
            } else if (['mp4', 'webm', 'ogg'].includes(fileExtension)) {
                var video = $('<video controls>').attr('src', url).css('maxWidth', '500px');
                previewContainer.append(video);
            } else if (['mp3', 'wav', 'ogg'].includes(fileExtension)) {
                var audio = $('<audio controls>').attr('src', url);
                previewContainer.append(audio);
            } else {
                previewContainer.text('File format not supported for preview.');
            }
        }
    });


//    $('#btnSaveAll').on('click', function ()
//    {
//        var subject = $('#subject').val();
//        var dimension = $('#dimension').val();
//        var lesson = $('#lesson').val();
//        var content = CKEDITOR.instances.content.getData();
//        var explaination = CKEDITOR.instances.Explaination.getData();
//        var link = $('#link').val();
//        var status = $('#status').val();
//        if (!content || !explaination)
//        {
//            alert('Please provide content and explaination');
//            return;
//        }
//        
//        
//    });
});

