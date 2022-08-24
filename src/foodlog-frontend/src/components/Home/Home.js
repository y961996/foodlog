import React from "react";
import Post from "../Post/Post";
import {useEffect, useState} from "react";
import "./Home.scss";
import {Box, Container} from "@mui/material";

function Home() {
    const [error, setError] = useState(null);
    const [isLoaded, setIsLoaded] = useState(false);
    const [postList, setPostList] = useState([]);

    // TODO: This is an example of how to get the response when you got an error
    //       Maybe put this in a method and use that to make requests.
    let responseClone;
    useEffect(() => {
        fetch("http://localhost:8080/api/v1/posts")
            .then(res => {
                responseClone = res.clone();
                return res.json();
            })
            .then(
                (result) => {
                    setIsLoaded(true);
                    setPostList(result);
                },
                (error) => {
                    setIsLoaded(true);
                    setError(error);
                    console.log(error, responseClone);
                    responseClone.text()
                        .then(bodyText => {
                            console.log("Received: " + bodyText);
                        })
                }
            )
    }, []);

    if(error) {
        return <div>Error!!!</div>;
    } else if(!isLoaded) {
        return <div>Loading...</div>
    } else {
        return(
            //TODO: This container doesn't work properly. (It won't expand with the posts and
            //      posts don't have margins between them.
            <div className="container">
                <Container fixed>
                    <Box sx={{ display: "flex", flexWrap: "wrap", justifyContent: "center", alignItems: "center",
                               bgcolor: '#cfe8fc', height: '100vh' }}>
                        {postList.map(post => (
                            <Post
                                userId={post.userId}
                                userName={post.userName}
                                title={post.title}
                                text={post.text}
                                imagePaths={post.imagePaths}
                                shortVideoPath={post.shortVideoPath}
                            />
                        ))}
                    </Box>
                </Container>
            </div>
        );
    }
}

export default Home;