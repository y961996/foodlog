import {useState} from 'react';
import { styled } from '@mui/material/styles';
import Card from '@mui/material/Card';
import CardHeader from '@mui/material/CardHeader';
import CardMedia from '@mui/material/CardMedia';
import CardContent from '@mui/material/CardContent';
import CardActions from '@mui/material/CardActions';
import Collapse from '@mui/material/Collapse';
import Avatar from '@mui/material/Avatar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import { red } from '@mui/material/colors';
import FavoriteIcon from '@mui/icons-material/Favorite';
import ShareIcon from '@mui/icons-material/Share';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import CommentIcon from '@mui/icons-material/Comment';
import MoreVertIcon from '@mui/icons-material/MoreVert';
import {Link} from "react-router-dom";

const ExpandMore = styled((props) => {
    const { expand, ...other } = props;
    return <IconButton {...other} />;
})(({ theme, expand }) => ({
    transform: !expand ? 'rotate(0deg)' : 'rotate(180deg)',
    marginLeft: 'auto',
    transition: theme.transitions.create('transform', {
        duration: theme.transitions.duration.shortest,
    }),
}));

const ExpandComments = styled( (props) => {
    const { expand, ...other } = props;
    return <IconButton {...other} />
})(({ theme, expand }) => ({
}));

function Post(props) {
    const {userId, userName, title, text, imagePaths, shortVideoPath} = props;
    const [expanded, setExpanded] = useState(false);
    const [expandedComments, setExpandedComments] = useState(false);
    const [liked, setLiked] = useState(false);

    const handleExpandClick = () => {
        setExpanded(!expanded);
    };

    const handleExpandCommentsClick = () => {
        setExpandedComments(!expandedComments);
    }

    const handleLike = () => {
        setLiked(!liked);
    }

    return(
        <div className="postContainer">
            <Card sx={{ width: 800 }}>
                <CardHeader
                    avatar={
                        <Link style={{ textDecoration: "none", boxShadow: "none", color: "white"}} to={{pathname: 'users/' + userId}}>
                            <Avatar sx={{ bgcolor: red[500] }} aria-label="recipe">
                                {userName.charAt(0).toUpperCase()}
                            </Avatar>
                        </Link>
                    }
                    action={
                        <IconButton aria-label="settings">
                            <MoreVertIcon />
                        </IconButton>
                    }
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
                    <IconButton onClick={handleLike} aria-label="add to favorites">
                        <FavoriteIcon sx={liked ? { color: "red" } : null} />
                    </IconButton>
                    <IconButton aria-label="share">
                        <ShareIcon />
                    </IconButton>
                    <ExpandComments
                        expand={expanded}
                        onClick={handleExpandCommentsClick}
                        aria-expanded={expandedComments}
                        aria-label="comments"
                    >
                        <CommentIcon />
                    </ExpandComments>
                    <ExpandMore
                        expand={expanded}
                        onClick={handleExpandClick}
                        aria-expanded={expanded}
                        aria-label="show more"
                    >
                        <ExpandMoreIcon />
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
                    <CardContent>
                        <Typography paragraph>Comments:</Typography>
                        <Typography paragraph>
                            This area will be for comments...
                        </Typography>
                    </CardContent>
                </Collapse>
            </Card>
        </div>
    );
}

export default Post;
