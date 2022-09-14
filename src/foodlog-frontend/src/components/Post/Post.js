import {useEffect, useRef, useState} from 'react';
import {styled} from '@mui/material/styles';
import Card from '@mui/material/Card';
import CardHeader from '@mui/material/CardHeader';
import CardMedia from '@mui/material/CardMedia';
import CardContent from '@mui/material/CardContent';
import CardActions from '@mui/material/CardActions';
import Collapse from '@mui/material/Collapse';
import Avatar from '@mui/material/Avatar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import FavoriteIcon from '@mui/icons-material/Favorite';
import ShareIcon from '@mui/icons-material/Share';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import CommentIcon from '@mui/icons-material/Comment';
import MoreVertIcon from '@mui/icons-material/MoreVert';
import {Link} from "react-router-dom";
import {Container} from "@mui/material";
import Comment from "../Comment/Comment";
import CommentForm from "../Comment/CommentForm";
import {DeleteWithAuth, PostWithAuth} from "../../services/HttpService";

const ExpandMore = styled((props) => {
    const {expand, ...other} = props;
    return <IconButton {...other} />;
})(({theme, expand}) => ({
    transform: !expand ? 'rotate(0deg)' : 'rotate(180deg)',
    marginLeft: 'auto',
    transition: theme.transitions.create('transform', {
        duration: theme.transitions.duration.shortest,
    }),
}));

const ExpandComments = styled((props) => {
    const {expand, ...other} = props;
    return <IconButton {...other} />
})(({theme, expand}) => ({}));

function Post(props) {
    const {postId, userId, userName, title, text, imagePaths, shortVideoPath, likes} = props;
    const [expanded, setExpanded] = useState(false);
    const [expandedComments, setExpandedComments] = useState(false);
    const [error, setError] = useState(null);
    const [isLoaded, setIsLoaded] = useState(false);
    const [commentList, setCommentList] = useState([]);
    const [isLiked, setIsLiked] = useState(false);
    const isInitialMount = useRef(true);
    const [likeCount, setLikeCount] = useState(likes.length);
    const [likeId, setLikeId] = useState(null);

    let disabled = localStorage.getItem("currentUser") === null;

    const handleExpandClick = () => {
        setExpanded(!expanded);
    };

    const handleExpandCommentsClick = () => {
        setExpandedComments(!expandedComments);
        refreshComments();
        console.log(commentList);
    }

    const handleLike = () => {
        setIsLiked(!isLiked);
        if (!isLiked) {
            saveLike();
            setLikeCount(likeCount + 1);
        } else {
            deleteLike();
            setLikeCount(likeCount - 1)
        }
    }

    const refreshComments = () => {
        fetch("/comments?postId=" + postId)
            .then(res => {
                return res.json();
            })
            .then((result) => {
                setIsLoaded(true);
                setCommentList(result);
            }, (error) => {
                setIsLoaded(true);
                setError(error);
            })
    }

    const saveLike = () => {
        PostWithAuth("/likes", {
            postId: postId,
            userId: localStorage.getItem("currentUser"),
        })
            .then((res) => res.json())
            .catch((err) => console.log("Error: " + err))
    }

    const deleteLike = () => {
        DeleteWithAuth("/likes/" + likeId)
            .catch((err) => console.log("Error: " + err))
    }

    const checkLikes = () => {
        let likeControl = likes.find((like) => "" + like.userId === localStorage.getItem("currentUser"));
        if (likeControl != null) {
            setLikeId(likeControl.id);
            setIsLiked(true);
        }
    }

    useEffect(() => {
        if (isInitialMount.current) isInitialMount.current = false; else refreshComments();
    }, [commentList]);

    useEffect(() => {
        checkLikes();
    }, []);

    return (<div className="postContainer">
        <Card sx={{width: 800, textAlign: "left", margin: 5}}>
            <CardHeader
                avatar={<Link style={{textDecoration: "none", boxShadow: "none", color: "white"}}
                              to={{pathname: 'users/' + userId}}>
                    <Avatar sx={{
                        background: 'linear-gradient(45deg, #2196F3 30%, #21CBF3 90%)', color: "white"
                    }} aria-label="recipe">
                        {userName.charAt(0).toUpperCase()}
                    </Avatar>
                </Link>}
                action={<IconButton aria-label="settings">
                    <MoreVertIcon/>
                </IconButton>}
                title={title}
                subheader="September 14, 2016"
            />
            <CardMedia
                component="img"
                height="194"
                image="https://picsum.photos/400"
                alt="Image 1"
            />
            <CardContent>
                <Typography variant="body2" color="text.secondary">
                    {text}
                </Typography>
            </CardContent>
            <CardActions disableSpacing>
                <IconButton disabled={disabled} onClick={handleLike} aria-label="add to favorites">
                    <FavoriteIcon sx={isLiked ? {color: "red"} : null}/>
                </IconButton>
                {likeCount}
                <IconButton aria-label="share">
                    <ShareIcon/>
                </IconButton>
                <ExpandComments
                    expand={expanded}
                    onClick={handleExpandCommentsClick}
                    aria-expanded={expandedComments}
                    aria-label="comments"
                >
                    <CommentIcon/>
                </ExpandComments>
                <ExpandMore
                    expand={expanded}
                    onClick={handleExpandClick}
                    aria-expanded={expanded}
                    aria-label="show more"
                >
                    <ExpandMoreIcon/>
                </ExpandMore>
            </CardActions>
            <Collapse in={expanded} timeout="auto" unmountOnExit>
                <CardContent>
                    <Typography paragraph>Method:</Typography>
                    <Typography paragraph>
                        Heat 1/2 cup of the broth in a pot until simmering, add saffron and set
                        aside for 10 minutes.
                    </Typography>
                </CardContent>
            </Collapse>
            <Collapse in={expandedComments} timeout="auto" unmountOnExit>
                <Container fixed>
                    {error ? "Error: " + error : isLoaded ? commentList.map(comment => (
                        <Comment userId={1} userName={"USER"} text={comment.text}></Comment>)) : "Loading..."}
                    {disabled ? "" :
                        <CommentForm userId={1} userName={"USER"} postId={postId}></CommentForm>}
                </Container>
            </Collapse>
        </Card>
    </div>);
}

export default Post;
