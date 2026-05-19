import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Groups from "./pages/Groups";
import Chat from "./pages/Chat";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/groups" element={<Groups />} />
        <Route path="/groups/:groupId" element={<Chat />} />
      </Routes>
    </BrowserRouter>
  );
}