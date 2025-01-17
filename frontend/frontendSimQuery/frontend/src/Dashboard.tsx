import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  Box,
  TextField,
  Button,
  Typography,
  List,
  ListItem,
  ListItemText,
  Drawer,
  Paper,
  AppBar,
  Toolbar,
  IconButton,
  ThemeProvider,
  createTheme,
  CssBaseline,
  Tooltip,
} from "@mui/material";
import {
  ExitToApp,
  Send as SendIcon,
  Add as AddIcon,
  Chat as ChatIcon,
} from "@mui/icons-material";

const darkTheme = createTheme({
  palette: {
    mode: "dark",
    primary: {
      main: "#6366f1",
    },
    secondary: {
      main: "#22d3ee",
    },
    background: {
      default: "#0f172a",
      paper: "rgba(30, 41, 59, 0.8)",
    },
  },
});

interface MessageMetadata {
  externalId: string;
  content: string;
  sender: "USER" | "CHATBOT_MODEL";
  sentAt: string;
}

interface ChatSession {
  externalId: string;
  title: string;
  messages: MessageMetadata[];
  timestamp: Date;
}

export const Dashboard = () => {
  const navigate = useNavigate();
  const [input, setInput] = useState<string>('');
  const [chatSessions, setChatSessions] = useState<ChatSession[]>([]);
  const [currentChatId, setCurrentChatId] = useState<string | undefined>(undefined);
  const [error, setError] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const updateChatTitle = async (chatExternalId: string, newTitle: string) => {
    try {
      const accessToken = localStorage.getItem('accessToken');
      if (!accessToken) {
        throw new Error('No access token found');
      }

      const response = await fetch(`http://localhost:8001/api/v1/chats/${chatExternalId}`, {
        method: 'PUT',
        headers: {
          'Authorization': `Bearer ${accessToken}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          title: newTitle
        })
      });

      if (!response.ok) {
        throw new Error('Failed to update chat title');
      }
    } catch (error) {
      console.error(`Error updating chat title for ${chatExternalId}:`, error);
    }
  };

  const generateTitleFromMessage = (message: string) => {
    // Take first 30 characters of the message and add ellipsis if needed
    const truncated = message.slice(0, 30);
    return truncated.length < message.length ? `${truncated}...` : truncated;
  };

  const fetchMessagesForChat = async (chatExternalId: string) => {
    try {
      const accessToken = localStorage.getItem('accessToken');
      if (!accessToken) {
        throw new Error('No access token found');
      }

      const response = await fetch(`http://localhost:8001/api/v1/chats/${chatExternalId}/messages`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${accessToken}`,
          'Content-Type': 'application/json'
        }
      });

      if (!response.ok) {
        throw new Error('Failed to fetch messages');
      }

      const messages: MessageMetadata[] = await response.json();

      // Sort messages by timestamp and ensure USER messages come before CHATBOT_MODEL messages
      const sortedMessages = messages.sort((a, b) => {
        // First compare timestamps
        const timeCompare = new Date(a.sentAt).getTime() - new Date(b.sentAt).getTime();
        if (timeCompare !== 0) return timeCompare;
        
        // If timestamps are equal, put USER messages first
        if (a?.sender === 'USER' && b?.sender === 'CHATBOT_MODEL') return -1;
        if (a?.sender === 'CHATBOT_MODEL' && b?.sender === 'USER') return 1;
        return 0;
      });
      
      // If there are messages and the chat title is still "New Chat", update it
      if (messages.length > 0) {
        const currentChat = chatSessions.find(chat => chat.externalId === chatExternalId);
        if (currentChat?.title === 'New Chat') {
          const firstUserMessage = messages.find(msg => msg?.sender === 'USER');
          if (firstUserMessage) {
            const newTitle = generateTitleFromMessage(firstUserMessage.content);
            await updateChatTitle(chatExternalId, newTitle);
            
            // Update local state
            setChatSessions(prevSessions => 
              prevSessions.map(session => 
                session.externalId === chatExternalId
                  ? { ...session, title: newTitle }
                  : session
              )
            );
          }
        }
      }
      
      return sortedMessages;
    } catch (error) {
      console.error(`Error fetching messages for chat ${chatExternalId}:`, error);
      return [];
    }
  };

  const fetchAndUpdateChatMessages = async (chatExternalId: string) => {
    const messages = await fetchMessagesForChat(chatExternalId);
    setChatSessions(prevSessions => 
      prevSessions.map(session => 
        session.externalId === chatExternalId
          ? { ...session, messages }
          : session
      )
    );
  };

  // Modified to fetch messages for each chat
  useEffect(() => {
    const fetchChats = async () => {
      try {
        const accessToken = localStorage.getItem('accessToken');
        if (!accessToken) {
          throw new Error('No access token found. Please log in again.');
        }
  
        const response = await fetch('http://localhost:8001/api/v1/chats', {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Content-Type': 'application/json'
          }
        });
  
        if (!response.ok) {
          throw new Error('Failed to fetch chats');
        }
  
        const chats = await response.json();
        
        // Initialize chats with empty message arrays and sort by creation date
        const initializedChats = chats
          .map((chat: any) => ({
            ...chat,
            messages: [],
            timestamp: new Date(chat.createdAt)
          }))
          .sort((a: ChatSession, b: ChatSession) => 
            b.timestamp.getTime() - a.timestamp.getTime()
          );
        
        setChatSessions(initializedChats);
  
        // Set the first chat as current if chats exist
        if (initializedChats.length > 0) {
          const firstChatId = initializedChats[0].externalId;
          setCurrentChatId(firstChatId);
          // Fetch messages for the first chat immediately
          await fetchAndUpdateChatMessages(firstChatId);
        }
      } catch (error) {
        console.error('Error fetching chats:', error);
        setError(error instanceof Error ? error.message : 'An unknown error occurred');
      }
    };
  
    fetchChats();
  }, []);

  // Add effect to fetch messages when current chat changes
  useEffect(() => {
    if (currentChatId) {
      fetchAndUpdateChatMessages(currentChatId);
    }
  }, [currentChatId]);

  const getCurrentChat = () => {
    if (currentChatId) {
      return chatSessions.find(chat => chat.externalId === currentChatId);
    }
    return chatSessions[0];
  };

  const handleLogout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    navigate('/login');
  };

  const createNewChat = async () => {
    try {
      const accessToken = localStorage.getItem('accessToken');
      if (!accessToken) {
        throw new Error('No access token found. Please log in again.');
      }

      const response = await fetch('http://localhost:8001/api/v1/chats', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${accessToken}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          title: 'New Chat'
        })
      });

      if (!response.ok) {
        throw new Error('Failed to create chat');
      }

      const newChat = await response.json();
      
      setChatSessions(prevSessions => [
        {
          ...newChat,
          messages: [],
          timestamp: new Date(newChat.createdAt)
        },
        ...prevSessions.sort((a, b) => b.timestamp.getTime() - a.timestamp.getTime())
      ]);

      setCurrentChatId(newChat.externalId);
      return newChat;
    } catch (error) {
      console.error('Error creating chat:', error);
      setError(error instanceof Error ? error.message : 'An unknown error occurred');
      return null;
    }
  };

  const generateChatTitle = (firstMessage: string) => {
    const words = firstMessage.split(' ').slice(0, 5);
    return words.join(' ') + '...';
  };

  const handleSend = async () => {
    if (!input.trim()) return;

    const currentChat = getCurrentChat();
    
    // Ensure we have a current chat with an externalId
    let chatToUse = currentChat;
    if (!chatToUse || !chatToUse.externalId) {
      const newChat = await createNewChat();
      if (!newChat) return;
      chatToUse = newChat;
    }

    setIsLoading(true);

    // First, add the user message to the UI immediately
    const tempUserMessage: MessageMetadata = {
      externalId: crypto.randomUUID(),
      content: input,
      sender: "USER",
      sentAt: new Date().toISOString()
    };

    setChatSessions(prevSessions => {
      return prevSessions.map(session => {
        if (session.externalId === chatToUse?.externalId) {
          const isFirstMessage = session.messages.length === 0;
          const newTitle = isFirstMessage ? generateTitleFromMessage(input) : session.title;
          
          if (isFirstMessage) {
            updateChatTitle(chatToUse?.externalId, newTitle);
          }

          return {
            ...session,
            title: newTitle,
            messages: [...session.messages, tempUserMessage]
          };
        }
        return session;
      });
    });

    try {
      const accessToken = localStorage.getItem('accessToken');
      const response = await fetch('http://localhost:8001/api/v1/messages', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${accessToken}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          chatExternalId: chatToUse?.externalId,
          content: input
        })
      });
      
      if (!response.ok) {
        throw new Error('Failed to send message');
      }
      
      const responseData = await response.json();
      
      // After getting the response, update only with the AI message
      setChatSessions(prevSessions => {
        return prevSessions.map(session => {
          if (session.externalId === chatToUse?.externalId) {
            return {
              ...session,
              messages: [
                ...session.messages, // This already includes the user message
                responseData.aiMessage // Only add the AI response
              ]
            };
          }
          return session;
        });
      });
    } catch (error) {
      console.error('Error sending message:', error);
      const errorMessage: MessageMetadata = {
        externalId: crypto.randomUUID(),
        content: 'An error occurred. Please try again.',
        sender: 'CHATBOT_MODEL',
        sentAt: new Date().toISOString()
      };
      
      setChatSessions(prevSessions => {
        return prevSessions.map(session => {
          if (session.externalId === chatToUse?.externalId) {
            return {
              ...session,
              messages: [...session.messages, errorMessage]
            };
          }
          return session;
        });
      });
    } finally {
      setIsLoading(false);
      setInput('');
    }
  };

  const handleKeyDown = (event: React.KeyboardEvent) => {
    if (event.key === "Enter" && !isLoading) {
      handleSend();
    }
  };

  return (
    <ThemeProvider theme={darkTheme}>
      <CssBaseline />
      <Box
        sx={{
          display: "flex",
          minHeight: "100vh",
          background: "linear-gradient(45deg, #0f172a 0%, #1e293b 100%)",
          position: "relative",
          overflow: "hidden",
        }}
      >
        {/* Animated background effects */}
        <Box
          sx={{
            position: "absolute",
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            backgroundImage: `
              radial-gradient(circle at 20% 30%, rgba(99, 102, 241, 0.15) 0%, transparent 30%),
              radial-gradient(circle at 80% 70%, rgba(34, 211, 238, 0.15) 0%, transparent 30%)
            `,
            animation: "gradient 15s ease infinite",
            "@keyframes gradient": {
              "0%": { transform: "scale(1, 1)" },
              "50%": { transform: "scale(1.2, 1.2)" },
              "100%": { transform: "scale(1, 1)" },
            },
          }}
        />

        {/* App Bar */}
        <AppBar
          position="fixed"
          sx={{
            zIndex: (theme) => theme.zIndex.drawer + 1,
            background: "transparent",
            backdropFilter: "blur(10px)",
            borderBottom: "1px solid rgba(255,255,255,0.1)",
          }}
          elevation={0}
        >
          <Toolbar sx={{ justifyContent: "space-between" }}>
            <Typography
              variant="h5"
              sx={{
                fontWeight: 700,
                background: "linear-gradient(45deg, #6366f1, #22d3ee)",
                backgroundClip: "text",
                WebkitBackgroundClip: "text",
                WebkitTextFillColor: "transparent",
              }}
            >
              SimQuery
            </Typography>
            <Button
              startIcon={<ExitToApp />}
              onClick={handleLogout}
              sx={{
                color: "#fff",
                borderRadius: 2,
                "&:hover": {
                  background: "rgba(99, 102, 241, 0.1)",
                },
              }}
            >
              Logout
            </Button>
          </Toolbar>
        </AppBar>

        {/* Side Drawer */}
        <Drawer
          variant="permanent"
          sx={{
            width: 280,
            flexShrink: 0,
            "& .MuiDrawer-paper": {
              width: 280,
              boxSizing: "border-box",
              background: "rgba(30, 41, 59, 0.8)",
              backdropFilter: "blur(10px)",
              borderRight: "1px solid rgba(255,255,255,0.1)",
            },
          }}
        >
          <Toolbar />
          <Box
            sx={{
              overflow: "auto",
              p: 2,
              position: "relative",
              height: "100%",
            }}
          >
            <Tooltip title="New Chat">
              <Button
                fullWidth
                startIcon={<AddIcon />}
                onClick={createNewChat}
                sx={{
                  mb: 2,
                  color: "#fff",
                  borderRadius: 2,
                  border: "1px solid rgba(255,255,255,0.1)",
                  background: "rgba(255,255,255,0.05)",
                  backdropFilter: "blur(10px)",
                  "&:hover": {
                    background: "rgba(99, 102, 241, 0.1)",
                    borderColor: "#6366f1",
                  },
                }}
              >
                New Chat
              </Button>
            </Tooltip>

            <List>
              {chatSessions.map((chat) => (
                <ListItem
                  key={chat.externalId}
                  onClick={() => setCurrentChatId(chat.externalId)}
                  sx={{
                    borderRadius: 2,
                    mb: 1,
                    background:
                      chat.externalId === currentChatId
                        ? "rgba(99, 102, 241, 0.1)"
                        : "rgba(255,255,255,0.05)",
                    backdropFilter: "blur(10px)",
                    transition: "all 0.3s ease",
                    cursor: "pointer",
                    "&:hover": {
                      background: "rgba(99, 102, 241, 0.1)",
                      transform: "translateX(5px)",
                    },
                  }}
                >
                  <ChatIcon sx={{ mr: 2, color: "rgba(255,255,255,0.7)" }} />
                  <ListItemText
                    primary={chat.title}
                    primaryTypographyProps={{
                      variant: "body2",
                      sx: {
                        color: "rgba(255,255,255,0.8)",
                      },
                    }}
                  />
                </ListItem>
              ))}
            </List>
          </Box>
        </Drawer>

        {/* Main Chat Area */}
        <Box
          component="main"
          sx={{
            flexGrow: 1,
            p: 3,
            position: "relative",
            display: "flex",
            flexDirection: "column",
            zIndex: 1,
          }}
        >
          <Toolbar />
          <Box
            flex={1}
            overflow="auto"
            mb={2}
            p={2}
            sx={{
              "&::-webkit-scrollbar": { display: "none" },
            }}
          >
            {getCurrentChat()?.messages?.map((message, index) => (
              <Box
                key={index}
                mb={2}
                display="flex"
                justifyContent={
                  message?.sender === "USER" ? "flex-end" : "flex-start"
                }
              >
                <Paper
                  elevation={0}
                  sx={{
                    p: 2,
                    maxWidth: "70%",
                    borderRadius: 3,
                    background:
                      message.sender === "USER"
                        ? "linear-gradient(45deg, #6366f1 30%, #22d3ee 90%)"
                        : "rgba(30, 41, 59, 0.8)",
                    backdropFilter: "blur(10px)",
                    color: "#fff",
                    transition: "all 0.3s ease",
                    "&:hover": {
                      transform: "translateY(-2px)",
                      boxShadow:
                        message.sender === "USER"
                          ? "0 6px 20px rgba(99, 102, 241, 0.3)"
                          : "0 6px 20px rgba(34, 211, 238, 0.3)",
                    },
                  }}
                >
<Typography>{message.content}</Typography>                </Paper>
              </Box>
            ))}
          </Box>

          {/* Input Area */}
          <Paper
            elevation={0}
            sx={{
              p: 2,
              background: "rgba(30, 41, 59, 0.8)",
              backdropFilter: "blur(10px)",
              borderRadius: 3,
              border: "1px solid rgba(255,255,255,0.1)",
            }}
          >
            <Box display="flex" gap={2}>
              <TextField
                fullWidth
                variant="outlined"
                value={input}
                onChange={(e) => setInput(e.target.value)}
                placeholder="Type your message..."
                onKeyDown={handleKeyDown}
                sx={{
                  "& .MuiOutlinedInput-root": {
                    borderRadius: 2,
                    backgroundColor: "rgba(255,255,255,0.05)",
                    "& fieldset": {
                      borderColor: "rgba(255,255,255,0.1)",
                    },
                    "&:hover fieldset": {
                      borderColor: "#6366f1",
                    },
                    "&.Mui-focused fieldset": {
                      borderColor: "#22d3ee",
                    },
                  },
                  "& .MuiOutlinedInput-input": {
                    color: "#fff",
                  },
                }}
              />
              <Button
                variant="contained"
                onClick={handleSend}
                disabled={isLoading}
                sx={{
                  borderRadius: 2,
                  px: 3,
                  background:
                    "linear-gradient(45deg, #6366f1 30%, #22d3ee 90%)",
                  boxShadow: "0 3px 5px 2px rgba(99, 102, 241, .3)",
                  transition: "all 0.3s ease-in-out",
                  "&:hover": {
                    transform: "translateY(-2px)",
                    boxShadow: "0 6px 20px 4px rgba(99, 102, 241, .3)",
                  },
                }}
              >
                <SendIcon />
              </Button>
            </Box>
          </Paper>
        </Box>
      </Box>
    </ThemeProvider>
  );
};

export default Dashboard;