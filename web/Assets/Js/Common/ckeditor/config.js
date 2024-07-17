/**
 * @license Copyright (c) 2003-2023, CKSource Holding sp. z o.o. All rights reserved.
 * For licensing, see https://ckeditor.com/legal/ckeditor-oss-license
 */

CKEDITOR.editorConfig = function (config) {
    // Define changes to default configuration here. For example:
    // config.language = 'fr';
    // config.uiColor = '#AADC6E';
    config.versionCheck = false;
    config.width = '100%';
    // fit the content with the width
    config.autoGrow_onStartup = true;
    config.autoGrow_minHeight = 200;
    config.autoGrow_maxHeight = 500;
    config.autoGrow_bottomSpace = 50;
    config.autoGrow_maxWidth = 1000;

    // basic toolbar with styling, font style, text alignment, lists, etc
    config.toolbar = [
        {name: 'styles', items: ['Format', 'Font', 'FontSize']},
        {name: 'basicstyles', items: ['Bold', 'Italic', 'Underline', 'Strike', 'Subscript', 'Superscript']},
        {name: 'colors', items: ['TextColor', 'BGColor']},
        {name: 'alignment', items: ['JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock']},
        {name: 'list', items: ['NumberedList', 'BulletedList']},
        {name: 'indent', items: ['Outdent', 'Indent']},
        {name: 'tools', items: ['Source']},
    ];

    // dont display the bottom bar
    config.removePlugins = 'elementspath';
    config.filebrowserUploadMethod = 'form';


};
