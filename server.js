import express from "express";
import fetch from "node-fetch";

const app = express();
app.use(express.json());

app.post("/api/summarize", async (req, res) => {
  const { url } = req.body;
  if (!url) return res.status(400).json({ error: "URL is required" });

  const apiKey = process.env.GEMINI_API_KEY;
  const apiUrl = `https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-preview-09-2025:generateContent?key=${apiKey}`;

  const body = {
    contents: [{ parts: [{ text: `Summarize the main content of: ${url}` }] }],
    systemInstruction: { parts: [{ text: "Provide a short, factual summary." }] }
  };

  try {
    const response = await fetch(apiUrl, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body),
    });

    const data = await response.json();
    const summary = data?.candidates?.[0]?.content?.parts?.[0]?.text || "No summary available.";
    res.json({ summary });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.listen(3000, () => console.log("Server running on port 3000"));
