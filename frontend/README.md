# Campus Activities Frontend

This is a minimal React frontend that interacts with the Campus Connect Backend.

Features:
- Fetches activities from the backend (GET /api/activities)
- Displays activities in cards
- Adds new activity via POST /api/activities using fetch API
- Handles loading and errors

Run locally:
1. cd frontend
2. npm install
3. npm start

By default the frontend talks to http://localhost:54434. You can override via environment variable REACT_APP_API_BASE.
