<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Slider Management</title>
        <link rel="stylesheet" href="./Assets/Styles/Marketing/SliderList.css">
        <link rel="icon" href="./Assets/Images/Common/logo.ico">
        <link rel="stylesheet" href="./Assets/Styles/main.css">
        <script src="./Assets/Scripts/SliderList.js"></script>
    </head>
    <body>
    <c:set scope="request" var="currentPage" value="Practices"/>
    <jsp:include page="../Common/HeaderView.jsp"></jsp:include>
        <main class="main-content">
            <header class="header">
                <div class="filter-sort-area">
                    <div class="filter-by">
                        <label for="status-filter">Filter by Status:</label>
                        <select id="status-filter" onchange="filterSliders()">
                            <option value="all">All</option>
                            <option value="show">Show</option>
                            <option value="hide">Hide</option>
                        </select>
                    </div>
                    <div class="controls">
                        <div class="slider-controls">
                            <div class="actions">
                                <input type="text" class="search-input" placeholder="Search by title or backlink" id="search-input">
                                <button class="btn" onclick="searchSliders()">Search</button>
                                <button class="btn">New Slider</button>
                            </div>
                        </div>
                    </div>
                </div>
            </header>
            <section class="slider-list">
                <table>
                    <thead>
                        <tr>
                            <th>Title</th>
                            <th>Image</th>
                            <th>Back_link</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody id="slider-table-body">
                    <c:forEach var="slider" items="${sliders}">
                    <tr>
                        <td>${slider.title}</td>
                        <td><img src="${slider.image}" alt="${slider.title}" width="100"></td>
                        <td>${slider.back_link}</td>
                        <td>${slider.status == 1 ? 'Show' : 'Hide'}</td>
                        <td>
                            <button onclick="viewSlider(${slider.slider_id})">View</button>
                            <button onclick="updateSliderStatus(${slider.slider_id}, ${slider.status == 1 ? 0 : 1})">
                                ${slider.status == 1 ? 'Hide' : 'Show'}
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </section>
    </main>
    <jsp:include page="../Common/FooterView.jsp"></jsp:include>
        <script>
            function searchSliders() {
                const query = document.getElementById('search-input').value;
                window.location.href = `slider-list?search=${query}`;
            }

            function filterSliders() {
                const status = document.getElementById('status-filter').value;
                window.location.href = `slider-list?status=${status}`;
            }

            function updateSliderStatus(sliderId, newStatus) {
                fetch(`slider-list?sliderId=${sliderId}&newStatus=${newStatus}`, {
                    method: 'POST'
                }).then(response => window.location.reload());
            }

            function viewSlider(sliderId) {
                window.location.href = `slider-details?sliderId=${sliderId}`;
            }
    </script>
</body>
</html>