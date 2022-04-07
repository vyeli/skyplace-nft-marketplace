<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="https://cdn.tailwindcss.com?plugins=forms"></script>
    <link href='https://fonts.googleapis.com/css?family=Montserrat' rel='stylesheet'>
    <style>
        body {
            font-family: 'Montserrat', serif;
        }
    </style>
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
                        padding: "1rem",
                        screens: {
                            lg: "1124px",
                            xl: "1124px",
                            "2xl": "1124px",
                        }
                    }
                }
            }
        }
    </script>
    <title>Skyplace</title>
</head>
