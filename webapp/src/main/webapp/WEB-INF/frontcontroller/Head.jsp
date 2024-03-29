<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="https://cdn.tailwindcss.com?plugins=forms,line-clamp"></script>
    <link rel="icon" href="<c:url value='/resources/logo.svg' />">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;500&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://unpkg.com/flowbite@1.4.5/dist/flowbite.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tw-elements/dist/css/index.min.css" />
    <style>
        body {
            font-family: 'Montserrat', sans-serif;
        }

        form {
            margin-bottom: 0;
        }

        input::-webkit-outer-spin-button,
        input::-webkit-inner-spin-button {
            -webkit-appearance: none;
            margin: 0;
        }

        /* Chrome, Safari, Edge, Opera */
        input::-webkit-outer-spin-button,
        input::-webkit-inner-spin-button {
          -webkit-appearance: none;
          margin: 0;
        }
        /*Firefox */
        input[type=number] {
            -moz-appearance: textfield;
        }
    </style>
    <script src="https://unpkg.com/flowbite@1.4.5/dist/flowbite.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/tw-elements/dist/js/index.min.js"></script>
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    animation: {
                        blob: "blob 7s infinite",
                    },
                    keyframes: {
                        blob: {
                            "0%": {
                                transform: "translate(0px, 0px) scale(1)",
                            },
                            "33%": {
                                transform: "translate(30px, -50px) scale(1.1)",
                            },
                            "66%": {
                                transform: "translate(-20px, 20px) scale(0.9)",
                            },
                            "100%": {
                                transform: "translate(0px, 0px) scale(1)",
                            },
                        },
                    },
                    colors: {
                        "bookmark-light-blue": "#0EA5E9",
                        "bookmark-blue": "#0369A1",
                        "bookmark-dark-blue": "#164E63",
                        "bookmark-grey": "#9194A2",
                        "bookmark-white": "#f7f7f7",
                    },
                    scale: {
                        '30': '0.30'
                    },
                    container: {
                        center: true,
                        screens: {
                            lg: "924px",
                            xl: "924px",
                            "2xl": "1124px",
                        }
                    },
                    gridTemplateColumns: {
                        'auto-fit': 'repeat(auto-fit, minmax(280px, 384px))'
                    },
                    lineClamp: {
                        4: '4',
                        18: '18'
                    }
                }
            }
        }
    </script>
    <title>Skyplace</title>
</head>
