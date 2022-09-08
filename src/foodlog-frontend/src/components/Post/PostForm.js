import {forwardRef, useState} from 'react';
import {styled} from '@mui/material/styles';
import Card from '@mui/material/Card';
import CardHeader from '@mui/material/CardHeader';
import CardMedia from '@mui/material/CardMedia';
import CardContent from '@mui/material/CardContent';
import Avatar from '@mui/material/Avatar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import MoreVertIcon from '@mui/icons-material/MoreVert';
import {Link} from "react-router-dom";
import {Button, InputAdornment, OutlinedInput, Snackbar} from "@mui/material";
import MuiAlert from '@mui/material/Alert';

const Alert = forwardRef(function Alert(props, ref) {
    return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

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

function PostForm(props) {
    const {userId, userName, refreshPosts} = props;
    const [text, setText] = useState("");
    const [title, setTitle] = useState("");
    const [isSent, setIsSent] = useState(false);

    const savePost = () => {
        fetch("/posts",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    title: title,
                    userId: userId,
                    text: text,
                }),
            })
            .then((res) => res.json())
            .catch((err) => console.log("err: " + err))
    }

    const handleSubmit = () => {
        savePost();
        setIsSent(true);
        setTitle("");
        setText("");
        refreshPosts();
    }

    const handleTitle = (value) => {
        setTitle(value);
        setIsSent(false);
    }

    const handleText = (value) => {
        setText(value);
        setIsSent(false);
    }

    const handleClose = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }

        setIsSent(false);
    };

    return (<div className="postContainer">
        <Snackbar open={isSent} autoHideDuration={1500} onClose={handleClose}>
            <Alert onClose={handleClose} severity="success" sx={{ width: '100%' }}>
                Your post has sent successfully!
            </Alert>
        </Snackbar>
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
                title={<OutlinedInput
                    id="outlined-adornment-amount"
                    multiline
                    placeholder="Title"
                    inputProps={{maxLength: 25}}
                    fullWidth
                    value={title}
                    onChange={(i) => handleTitle(i.target.value)}
                >
                </OutlinedInput>}
                subheader="September 14, 2016"
            />
            {//TODO: Add image picker here instead of CardMedia
                //      https://codesandbox.io/s/vj1q68zm25
                //      https://stackoverflow.com/questions/40589302/how-to-enable-file-upload-on-reacts-material-ui-simple-input
            }
            <CardMedia
                component="img"
                height="194"
                image="https://picsum.photos/400"
                alt="Image 1"
            />
            <CardContent>
                <Typography variant="body2" color="text.secondary">
                    {<OutlinedInput
                        id="outlined-adornment-amount"
                        multiline
                        placeholder="Text"
                        inputProps={{maxLength: 250}}
                        fullWidth
                        value={text}
                        onChange={(i) => handleText(i.target.value)}
                        endAdornment={<InputAdornment position="end">
                            <Button
                                variant="contained"
                                style={{
                                    background: 'linear-gradient(45deg, #2196F3 30%, #21CBF3 90%)', color: "white"
                                }}
                                onClick={handleSubmit}
                            >
                                Post
                            </Button>
                        </InputAdornment>}
                    >
                    </OutlinedInput>}
                </Typography>
            </CardContent>
        </Card>
    </div>);
}

export default PostForm;
