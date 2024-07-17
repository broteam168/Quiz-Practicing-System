package Filters;

import Models.MenuRole;
import Models.Role;
import Models.User;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SecurityConfig {

    /// For singeleton object
    private static SecurityConfig instance;

    /// Add secure protect link config
    private final Map<String, List<String>> roleUrlMap = new HashMap<>();

    {
        /// Basic init object list
        roleUrlMap.put("Guest", new ArrayList<>());
        roleUrlMap.put("Customer", new ArrayList<>());
        roleUrlMap.put("Marketing", new ArrayList<>());
        roleUrlMap.put("Sale", new ArrayList<>());
        roleUrlMap.put("Expert", new ArrayList<>());
        roleUrlMap.put("Admin", new ArrayList<>());
        roleUrlMap.put("Unknown", new ArrayList<>());
    }

    private SecurityConfig() {

    }

    public void AddProtectLink(String url, int role) {
        String roleString = Role.getRoleName(role);
        // Retreive exist and put again with new url
        List<String> existUrl = roleUrlMap.get(roleString);
        existUrl.add(url);
        roleUrlMap.put(roleString, existUrl);
    }

    public void RemoveProtectLink(String url, int role) {
        try {
            String roleString = Role.getRoleName(role);

            List<String> existUrl = roleUrlMap.get(roleString);
            existUrl.remove(url);
            roleUrlMap.put(roleString, existUrl);
        } catch (Exception ex) {

        }
    }

    public boolean IsValid(String url, User currentUser) {
        if (roleUrlMap.get(Role.getRoleName(Role.ROLE_GUEST))
                .stream().anyMatch(x -> x.contains(url))) {
            return true;
        } else {
            if (currentUser == null) {
                return false;
            } else {
                int role = currentUser.getRole();

                boolean isValid = roleUrlMap.get(Role.getRoleName(role))
                        .stream().anyMatch(x -> x.contains(url));

                return isValid;
            }
        }
    }

    public boolean IsNotFound(String url) {
        Set<String> keys = roleUrlMap.keySet();
        for (var key : keys) {
            if (roleUrlMap.get(key).contains(url)) {
                return false;
            }
        }
        return true;
    }

    public static SecurityConfig GetInstance() {
        if (instance == null) {
            instance = new SecurityConfig();
        }
        return instance;
    }

    public List<Entry<String, String>> GetRoleMenu(int role) {
        // Get menu data for each role
        switch (role) {
            case Role.ROLE_GUEST -> {
                /// 1 entry = 1 menu item name + link
                return new ArrayList<Entry<String, String>>() {
                    {
                        add(new SimpleEntry<String, String>("Home", "home"));
                        add(new SimpleEntry<String, String>("Blogs", "bloglist"));
                        add(new SimpleEntry<String, String>("Subjects", "subjectlist"));
                    }
                };
            }
            case Role.ROLE_CUSTOMER -> {
                return new ArrayList<Entry<String, String>>() {
                    {
                        add(new SimpleEntry<String, String>("Home", "home"));
                        add(new SimpleEntry<String, String>("Blogs", "bloglist"));
                        add(new SimpleEntry<String, String>("Subjects", "subjectlist"));
                        add(new SimpleEntry<String, String>("Practices", "practicelist"));

                    }
                };
            }
            case Role.ROLE_ADMIN -> {
                return new ArrayList<Entry<String, String>>() {
                    {
                        add(new SimpleEntry<String, String>("Home", "home"));
                        add(new SimpleEntry<String, String>("Dashboard", "dashboard"));
                        add(new SimpleEntry<String, String>("Blogs", "bloglist"));
                        add(new SimpleEntry<String, String>("Subjects", "subjectlist"));

                    }
                };
            }
            case Role.ROLE_MARKETING -> {
                return new ArrayList<Entry<String, String>>() {
                    {
                        add(new SimpleEntry<String, String>("Home", "home"));
                        add(new SimpleEntry<String, String>("Dashboard", "dashboard"));
                        add(new SimpleEntry<String, String>("Blogs", "bloglist"));
                        add(new SimpleEntry<String, String>("Subjects", "subjectlist"));

                    }
                };
            }
            case Role.ROLE_SALE -> {
                return new ArrayList<Entry<String, String>>() {
                    {
                        add(new SimpleEntry<String, String>("Home", "home"));
                        add(new SimpleEntry<String, String>("Dashboard", "dashboard"));
                        add(new SimpleEntry<String, String>("Blogs", "bloglist"));
                        add(new SimpleEntry<String, String>("Subjects", "subjectlist"));

                    }
                };
            }
            case Role.ROLE_EXPERT -> {
                return new ArrayList<Entry<String, String>>() {
                    {
                        add(new SimpleEntry<String, String>("Home", "home"));
                        add(new SimpleEntry<String, String>("Dashboard", "subjectlistedit"));
                        add(new SimpleEntry<String, String>("Blogs", "bloglist"));
                        add(new SimpleEntry<String, String>("Subjects", "subjectlist"));
                        add(new SimpleEntry<String, String>("Practices", "practicelist"));
                    }
                };
            }
            default -> {
                /// 1 entry = 1 menu item name + link
                return new ArrayList<Entry<String, String>>() {
                    {
                        add(new SimpleEntry<String, String>("Home", "home"));
                        add(new SimpleEntry<String, String>("Blogs", "bloglist"));
                        add(new SimpleEntry<String, String>("Subjects", "subjectlist"));
                        add(new SimpleEntry<String, String>("Practices", "practicelist"));

                    }
                };
            }
        }
    }

    public List<MenuRole> GetRoleMenuForSideNavBar(int role) {
        ArrayList<MenuRole> list = new ArrayList<>();
        //Get menu data for each role
        switch (role) {
            case Role.ROLE_MARKETING -> {
                list.add(new MenuRole("Home", "home", "<svg class=\"profile-svg\" xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 576 512\"><!--!Font Awesome Free 6.5.2 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2024 Fonticons, Inc.--><path fill=\"#ffffff\" d=\"M575.8 255.5c0 18-15 32.1-32 32.1h-32l.7 160.2c0 2.7-.2 5.4-.5 8.1V472c0 22.1-17.9 40-40 40H456c-1.1 0-2.2 0-3.3-.1c-1.4 .1-2.8 .1-4.2 .1H416 392c-22.1 0-40-17.9-40-40V448 384c0-17.7-14.3-32-32-32H256c-17.7 0-32 14.3-32 32v64 24c0 22.1-17.9 40-40 40H160 128.1c-1.5 0-3-.1-4.5-.2c-1.2 .1-2.4 .2-3.6 .2H104c-22.1 0-40-17.9-40-40V360c0-.9 0-1.9 .1-2.8V287.6H32c-18 0-32-14-32-32.1c0-9 3-17 10-24L266.4 8c7-7 15-8 22-8s15 2 21 7L564.8 231.5c8 7 12 15 11 24z\"/></svg>"));
                list.add(new MenuRole("Dashboard", "dashboard", "<svg class=\"dashboard-svg\" enable-background=\"new 0 0 32 32\" height=\"32px\" id=\"Layer_1\" version=\"1.1\" viewBox=\"0 0 32 32\" width=\"32px\" xml:space=\"preserve\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\"><g><path d=\"M15.42,7.221c0-0.951-0.771-1.721-1.721-1.721H6.729c-0.951,0-1.721,0.771-1.721,1.721v6.103   c0,0.951,0.771,1.721,1.721,1.721h6.971c0.951,0,1.721-0.771,1.721-1.721V7.221z\" fill=\"#ffffff\"/><path d=\"M27.742,7.221c0-0.951-0.77-1.721-1.721-1.721h-6.971c-0.951,0-1.721,0.771-1.721,1.721v6.103   c0,0.951,0.77,1.721,1.721,1.721h6.971c0.951,0,1.721-0.771,1.721-1.721V7.221z\" fill=\"#ffffff\"/><path d=\"M15.42,18.676c0-0.951-0.771-1.721-1.721-1.721H6.729c-0.951,0-1.721,0.77-1.721,1.721v6.104   c0,0.95,0.771,1.721,1.721,1.721h6.971c0.951,0,1.721-0.771,1.721-1.721V18.676z\" fill=\"#ffffff\"/><path d=\"M27.742,18.676c0-0.951-0.77-1.721-1.721-1.721h-6.971c-0.951,0-1.721,0.77-1.721,1.721v6.104   c0,0.95,0.77,1.721,1.721,1.721h6.971c0.951,0,1.721-0.771,1.721-1.721V18.676z\" fill=\"#ffffff\"/></g></svg>"));
                list.add(new MenuRole("Posts List", "postlist", "<svg class=\"profile-svg\" width=\"20\" height=\"19\" viewBox=\"0 0 20 19\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">\n"
                        + "<path d=\"M20 7.39969C20.0004 7.11991 19.9289 6.84501 19.7928 6.60294C19.6567 6.36086 19.4608 6.16025 19.2251 6.0215L10.8012 0.869378C10.5584 0.723403 10.2824 0.646484 10.0013 0.646484C9.72025 0.646484 9.44422 0.723403 9.20144 0.869378L0.765064 6.0215C0.531934 6.16406 0.338811 6.36668 0.204673 6.60944C0.0705351 6.85221 0 7.12677 0 7.40613C0 7.6855 0.0705351 7.96005 0.204673 8.20282C0.338811 8.44559 0.531934 8.6482 0.765064 8.79076L3.16474 10.2334V13.8527C3.16619 14.1356 3.23823 14.4133 3.37394 14.6592C3.50965 14.9051 3.70446 15.1108 3.93964 15.2567L9.25143 18.4123C9.4886 18.5477 9.75524 18.6187 10.0263 18.6187C10.2974 18.6187 10.5641 18.5477 10.8012 18.4123L16.113 15.2567C16.3482 15.1108 16.543 14.9051 16.6787 14.6592C16.8144 14.4133 16.8865 14.1356 16.8879 13.8527V10.1947L18.4377 9.25445V12.7579H20V7.39969ZM15.3381 13.8398L10.0013 17.0084L4.72704 13.8527V11.1865L9.20144 13.93C9.44505 14.0735 9.72076 14.1489 10.0013 14.1489C10.2819 14.1489 10.5576 14.0735 10.8012 13.93L15.3256 11.1479L15.3381 13.8398ZM10.0013 12.5389L1.56496 7.38681L10.0013 2.22181L18.4377 7.37393L10.0013 12.5389Z\" fill=\"white\"/>\n"
                        + "</svg>"));
                return list;
            }
            case Role.ROLE_SALE -> {
                list.add(new MenuRole("Home", "home", "<svg class=\"profile-svg\" xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 576 512\"><!--!Font Awesome Free 6.5.2 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2024 Fonticons, Inc.--><path fill=\"#ffffff\" d=\"M575.8 255.5c0 18-15 32.1-32 32.1h-32l.7 160.2c0 2.7-.2 5.4-.5 8.1V472c0 22.1-17.9 40-40 40H456c-1.1 0-2.2 0-3.3-.1c-1.4 .1-2.8 .1-4.2 .1H416 392c-22.1 0-40-17.9-40-40V448 384c0-17.7-14.3-32-32-32H256c-17.7 0-32 14.3-32 32v64 24c0 22.1-17.9 40-40 40H160 128.1c-1.5 0-3-.1-4.5-.2c-1.2 .1-2.4 .2-3.6 .2H104c-22.1 0-40-17.9-40-40V360c0-.9 0-1.9 .1-2.8V287.6H32c-18 0-32-14-32-32.1c0-9 3-17 10-24L266.4 8c7-7 15-8 22-8s15 2 21 7L564.8 231.5c8 7 12 15 11 24z\"/></svg>"));
                list.add(new MenuRole("Registration Management", "registration-list", "<svg class=\"profile-svg\" width=\"20\" height=\"19\" viewBox=\"0 0 20 19\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">\n"
                        + "<path d=\"M20 7.39969C20.0004 7.11991 19.9289 6.84501 19.7928 6.60294C19.6567 6.36086 19.4608 6.16025 19.2251 6.0215L10.8012 0.869378C10.5584 0.723403 10.2824 0.646484 10.0013 0.646484C9.72025 0.646484 9.44422 0.723403 9.20144 0.869378L0.765064 6.0215C0.531934 6.16406 0.338811 6.36668 0.204673 6.60944C0.0705351 6.85221 0 7.12677 0 7.40613C0 7.6855 0.0705351 7.96005 0.204673 8.20282C0.338811 8.44559 0.531934 8.6482 0.765064 8.79076L3.16474 10.2334V13.8527C3.16619 14.1356 3.23823 14.4133 3.37394 14.6592C3.50965 14.9051 3.70446 15.1108 3.93964 15.2567L9.25143 18.4123C9.4886 18.5477 9.75524 18.6187 10.0263 18.6187C10.2974 18.6187 10.5641 18.5477 10.8012 18.4123L16.113 15.2567C16.3482 15.1108 16.543 14.9051 16.6787 14.6592C16.8144 14.4133 16.8865 14.1356 16.8879 13.8527V10.1947L18.4377 9.25445V12.7579H20V7.39969ZM15.3381 13.8398L10.0013 17.0084L4.72704 13.8527V11.1865L9.20144 13.93C9.44505 14.0735 9.72076 14.1489 10.0013 14.1489C10.2819 14.1489 10.5576 14.0735 10.8012 13.93L15.3256 11.1479L15.3381 13.8398ZM10.0013 12.5389L1.56496 7.38681L10.0013 2.22181L18.4377 7.37393L10.0013 12.5389Z\" fill=\"white\"/>\n"
                        + "</svg>"));
                return list;
            }
            case Role.ROLE_ADMIN -> {
                list.add(new MenuRole("Home", "home", "<svg class=\"profile-svg\" xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 576 512\"><!--!Font Awesome Free 6.5.2 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2024 Fonticons, Inc.--><path fill=\"#ffffff\" d=\"M575.8 255.5c0 18-15 32.1-32 32.1h-32l.7 160.2c0 2.7-.2 5.4-.5 8.1V472c0 22.1-17.9 40-40 40H456c-1.1 0-2.2 0-3.3-.1c-1.4 .1-2.8 .1-4.2 .1H416 392c-22.1 0-40-17.9-40-40V448 384c0-17.7-14.3-32-32-32H256c-17.7 0-32 14.3-32 32v64 24c0 22.1-17.9 40-40 40H160 128.1c-1.5 0-3-.1-4.5-.2c-1.2 .1-2.4 .2-3.6 .2H104c-22.1 0-40-17.9-40-40V360c0-.9 0-1.9 .1-2.8V287.6H32c-18 0-32-14-32-32.1c0-9 3-17 10-24L266.4 8c7-7 15-8 22-8s15 2 21 7L564.8 231.5c8 7 12 15 11 24z\"/></svg>"));
                list.add(new MenuRole("Dashboard", "dashboard", "<svg class=\"dashboard-svg\" enable-background=\"new 0 0 32 32\" height=\"32px\" id=\"Layer_1\" version=\"1.1\" viewBox=\"0 0 32 32\" width=\"32px\" xml:space=\"preserve\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\"><g><path d=\"M15.42,7.221c0-0.951-0.771-1.721-1.721-1.721H6.729c-0.951,0-1.721,0.771-1.721,1.721v6.103   c0,0.951,0.771,1.721,1.721,1.721h6.971c0.951,0,1.721-0.771,1.721-1.721V7.221z\" fill=\"#ffffff\"/><path d=\"M27.742,7.221c0-0.951-0.77-1.721-1.721-1.721h-6.971c-0.951,0-1.721,0.771-1.721,1.721v6.103   c0,0.951,0.77,1.721,1.721,1.721h6.971c0.951,0,1.721-0.771,1.721-1.721V7.221z\" fill=\"#ffffff\"/><path d=\"M15.42,18.676c0-0.951-0.771-1.721-1.721-1.721H6.729c-0.951,0-1.721,0.77-1.721,1.721v6.104   c0,0.95,0.771,1.721,1.721,1.721h6.971c0.951,0,1.721-0.771,1.721-1.721V18.676z\" fill=\"#ffffff\"/><path d=\"M27.742,18.676c0-0.951-0.77-1.721-1.721-1.721h-6.971c-0.951,0-1.721,0.77-1.721,1.721v6.104   c0,0.95,0.77,1.721,1.721,1.721h6.971c0.951,0,1.721-0.771,1.721-1.721V18.676z\" fill=\"#ffffff\"/></g></svg>"));
                list.add(new MenuRole("Subject List", "subjectlistedit", "<svg class=\"profile-svg\" width=\"20\" height=\"19\" viewBox=\"0 0 20 19\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">\n"
                        + "<path d=\"M20 7.39969C20.0004 7.11991 19.9289 6.84501 19.7928 6.60294C19.6567 6.36086 19.4608 6.16025 19.2251 6.0215L10.8012 0.869378C10.5584 0.723403 10.2824 0.646484 10.0013 0.646484C9.72025 0.646484 9.44422 0.723403 9.20144 0.869378L0.765064 6.0215C0.531934 6.16406 0.338811 6.36668 0.204673 6.60944C0.0705351 6.85221 0 7.12677 0 7.40613C0 7.6855 0.0705351 7.96005 0.204673 8.20282C0.338811 8.44559 0.531934 8.6482 0.765064 8.79076L3.16474 10.2334V13.8527C3.16619 14.1356 3.23823 14.4133 3.37394 14.6592C3.50965 14.9051 3.70446 15.1108 3.93964 15.2567L9.25143 18.4123C9.4886 18.5477 9.75524 18.6187 10.0263 18.6187C10.2974 18.6187 10.5641 18.5477 10.8012 18.4123L16.113 15.2567C16.3482 15.1108 16.543 14.9051 16.6787 14.6592C16.8144 14.4133 16.8865 14.1356 16.8879 13.8527V10.1947L18.4377 9.25445V12.7579H20V7.39969ZM15.3381 13.8398L10.0013 17.0084L4.72704 13.8527V11.1865L9.20144 13.93C9.44505 14.0735 9.72076 14.1489 10.0013 14.1489C10.2819 14.1489 10.5576 14.0735 10.8012 13.93L15.3256 11.1479L15.3381 13.8398ZM10.0013 12.5389L1.56496 7.38681L10.0013 2.22181L18.4377 7.37393L10.0013 12.5389Z\" fill=\"white\"/>\n"
                        + "</svg>"));
                list.add(new MenuRole("Quiz List", "quizlist", "<svg width=\"20\" height=\"20\" xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 576 512\"><!--!Font Awesome Free 6.5.2 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2024 Fonticons, Inc.--><path fill=\"#ffffff\" d=\"M88.7 223.8L0 375.8V96C0 60.7 28.7 32 64 32H181.5c17 0 33.3 6.7 45.3 18.7l26.5 26.5c12 12 28.3 18.7 45.3 18.7H416c35.3 0 64 28.7 64 64v32H144c-22.8 0-43.8 12.1-55.3 31.8zm27.6 16.1C122.1 230 132.6 224 144 224H544c11.5 0 22 6.1 27.7 16.1s5.7 22.2-.1 32.1l-112 192C453.9 474 443.4 480 432 480H32c-11.5 0-22-6.1-27.7-16.1s-5.7-22.2 .1-32.1l112-192z\"/></svg>"));
                list.add(new MenuRole("Question List", "question", "<svg xmlns=\"http://www.w3.org/2000/svg\" height=\"19\" width=\"20\" viewBox=\"0 0 576 512\"><!--!Font Awesome Free 6.5.2 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2024 Fonticons, Inc.--><path fill=\"#ffffff\" d=\"M0 64C0 28.7 28.7 0 64 0H224V128c0 17.7 14.3 32 32 32H384v38.6C310.1 219.5 256 287.4 256 368c0 59.1 29.1 111.3 73.7 143.3c-3.2 .5-6.4 .7-9.7 .7H64c-35.3 0-64-28.7-64-64V64zm384 64H256V0L384 128zm48 96a144 144 0 1 1 0 288 144 144 0 1 1 0-288zm0 240a24 24 0 1 0 0-48 24 24 0 1 0 0 48zM368 321.6V328c0 8.8 7.2 16 16 16s16-7.2 16-16v-6.4c0-5.3 4.3-9.6 9.6-9.6h40.5c7.7 0 13.9 6.2 13.9 13.9c0 5.2-2.9 9.9-7.4 12.3l-32 16.8c-5.3 2.8-8.6 8.2-8.6 14.2V384c0 8.8 7.2 16 16 16s16-7.2 16-16v-5.1l23.5-12.3c15.1-7.9 24.5-23.6 24.5-40.6c0-25.4-20.6-45.9-45.9-45.9H409.6c-23 0-41.6 18.6-41.6 41.6z\"/></svg>"));
                return list;
            }
            case Role.ROLE_EXPERT -> {
                list.add(new MenuRole("Home", "home", "<svg class=\"profile-svg\" xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 576 512\"><!--!Font Awesome Free 6.5.2 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2024 Fonticons, Inc.--><path fill=\"#ffffff\" d=\"M575.8 255.5c0 18-15 32.1-32 32.1h-32l.7 160.2c0 2.7-.2 5.4-.5 8.1V472c0 22.1-17.9 40-40 40H456c-1.1 0-2.2 0-3.3-.1c-1.4 .1-2.8 .1-4.2 .1H416 392c-22.1 0-40-17.9-40-40V448 384c0-17.7-14.3-32-32-32H256c-17.7 0-32 14.3-32 32v64 24c0 22.1-17.9 40-40 40H160 128.1c-1.5 0-3-.1-4.5-.2c-1.2 .1-2.4 .2-3.6 .2H104c-22.1 0-40-17.9-40-40V360c0-.9 0-1.9 .1-2.8V287.6H32c-18 0-32-14-32-32.1c0-9 3-17 10-24L266.4 8c7-7 15-8 22-8s15 2 21 7L564.8 231.5c8 7 12 15 11 24z\"/></svg>"));
                list.add(new MenuRole("Subject List", "subjectlistedit", "<svg class=\"profile-svg\" width=\"20\" height=\"20\" viewBox=\"0 0 20 19\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">\n"
                        + "<path d=\"M20 7.39969C20.0004 7.11991 19.9289 6.84501 19.7928 6.60294C19.6567 6.36086 19.4608 6.16025 19.2251 6.0215L10.8012 0.869378C10.5584 0.723403 10.2824 0.646484 10.0013 0.646484C9.72025 0.646484 9.44422 0.723403 9.20144 0.869378L0.765064 6.0215C0.531934 6.16406 0.338811 6.36668 0.204673 6.60944C0.0705351 6.85221 0 7.12677 0 7.40613C0 7.6855 0.0705351 7.96005 0.204673 8.20282C0.338811 8.44559 0.531934 8.6482 0.765064 8.79076L3.16474 10.2334V13.8527C3.16619 14.1356 3.23823 14.4133 3.37394 14.6592C3.50965 14.9051 3.70446 15.1108 3.93964 15.2567L9.25143 18.4123C9.4886 18.5477 9.75524 18.6187 10.0263 18.6187C10.2974 18.6187 10.5641 18.5477 10.8012 18.4123L16.113 15.2567C16.3482 15.1108 16.543 14.9051 16.6787 14.6592C16.8144 14.4133 16.8865 14.1356 16.8879 13.8527V10.1947L18.4377 9.25445V12.7579H20V7.39969ZM15.3381 13.8398L10.0013 17.0084L4.72704 13.8527V11.1865L9.20144 13.93C9.44505 14.0735 9.72076 14.1489 10.0013 14.1489C10.2819 14.1489 10.5576 14.0735 10.8012 13.93L15.3256 11.1479L15.3381 13.8398ZM10.0013 12.5389L1.56496 7.38681L10.0013 2.22181L18.4377 7.37393L10.0013 12.5389Z\" fill=\"white\"/>\n"
                        + "</svg>"));
                list.add(new MenuRole("Quiz List", "quizlist", "<svg width=\"20\" height=\"19\" xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 576 512\"><!--!Font Awesome Free 6.5.2 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2024 Fonticons, Inc.--><path fill=\"#ffffff\" d=\"M88.7 223.8L0 375.8V96C0 60.7 28.7 32 64 32H181.5c17 0 33.3 6.7 45.3 18.7l26.5 26.5c12 12 28.3 18.7 45.3 18.7H416c35.3 0 64 28.7 64 64v32H144c-22.8 0-43.8 12.1-55.3 31.8zm27.6 16.1C122.1 230 132.6 224 144 224H544c11.5 0 22 6.1 27.7 16.1s5.7 22.2-.1 32.1l-112 192C453.9 474 443.4 480 432 480H32c-11.5 0-22-6.1-27.7-16.1s-5.7-22.2 .1-32.1l112-192z\"/></svg>"));
                list.add(new MenuRole("Question List", "question", "<svg xmlns=\"http://www.w3.org/2000/svg\" height=\"19\" width=\"20\" viewBox=\"0 0 576 512\"><!--!Font Awesome Free 6.5.2 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2024 Fonticons, Inc.--><path fill=\"#ffffff\" d=\"M0 64C0 28.7 28.7 0 64 0H224V128c0 17.7 14.3 32 32 32H384v38.6C310.1 219.5 256 287.4 256 368c0 59.1 29.1 111.3 73.7 143.3c-3.2 .5-6.4 .7-9.7 .7H64c-35.3 0-64-28.7-64-64V64zm384 64H256V0L384 128zm48 96a144 144 0 1 1 0 288 144 144 0 1 1 0-288zm0 240a24 24 0 1 0 0-48 24 24 0 1 0 0 48zM368 321.6V328c0 8.8 7.2 16 16 16s16-7.2 16-16v-6.4c0-5.3 4.3-9.6 9.6-9.6h40.5c7.7 0 13.9 6.2 13.9 13.9c0 5.2-2.9 9.9-7.4 12.3l-32 16.8c-5.3 2.8-8.6 8.2-8.6 14.2V384c0 8.8 7.2 16 16 16s16-7.2 16-16v-5.1l23.5-12.3c15.1-7.9 24.5-23.6 24.5-40.6c0-25.4-20.6-45.9-45.9-45.9H409.6c-23 0-41.6 18.6-41.6 41.6z\"/></svg>"));

                return list;
            }
            default -> {
                list.add(new MenuRole("Home", "home", "<svg class=\"profile-svg\" xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 576 512\"><!--!Font Awesome Free 6.5.2 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2024 Fonticons, Inc.--><path fill=\"#ffffff\" d=\"M575.8 255.5c0 18-15 32.1-32 32.1h-32l.7 160.2c0 2.7-.2 5.4-.5 8.1V472c0 22.1-17.9 40-40 40H456c-1.1 0-2.2 0-3.3-.1c-1.4 .1-2.8 .1-4.2 .1H416 392c-22.1 0-40-17.9-40-40V448 384c0-17.7-14.3-32-32-32H256c-17.7 0-32 14.3-32 32v64 24c0 22.1-17.9 40-40 40H160 128.1c-1.5 0-3-.1-4.5-.2c-1.2 .1-2.4 .2-3.6 .2H104c-22.1 0-40-17.9-40-40V360c0-.9 0-1.9 .1-2.8V287.6H32c-18 0-32-14-32-32.1c0-9 3-17 10-24L266.4 8c7-7 15-8 22-8s15 2 21 7L564.8 231.5c8 7 12 15 11 24z\"/></svg>"));
                return list;
            }
        }
    }
}
