import React from "react";
import Post from "../Post/Post";
import {useEffect, useState} from "react";
import "./Home.scss";
import {Box, Container} from "@mui/material";

function Home() {
    const [error, setError] = useState(null);
    const [isLoaded, setIsLoaded] = useState(false);
    const [postList, setPostList] = useState([]);

    useEffect(() => {
        fetch("/posts")
            .then(res => res.json())
            .then(
                (result) => {
                    setIsLoaded(true);
                    setPostList(result);
                },
                (error) => {
                    setIsLoaded(true);
                    setError(error);
                    console.log(error);
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