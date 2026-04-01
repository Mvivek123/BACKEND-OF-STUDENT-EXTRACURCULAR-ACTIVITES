import React, { useEffect, useState } from 'react';
import ActivityCard from './ActivityCard';
import ActivityForm from './ActivityForm';

const API_BASE = process.env.REACT_APP_API_BASE || 'http://localhost:54434';

export default function ActivitiesList() {
  const [activities, setActivities] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchActivities = async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch(`${API_BASE}/api/activities`);
      if (!res.ok) throw new Error(`Server returned ${res.status}`);
      const data = await res.json();
      setActivities(data);
    } catch (e) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchActivities();
  }, []);

  const addActivity = async (activity) => {
    setError(null);
    try {
      const res = await fetch(`${API_BASE}/api/activities`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(activity),
      });
      if (!res.ok) {
        const text = await res.text();
        throw new Error(`Create failed: ${res.status} ${text}`);
      }
      const created = await res.json();
      setActivities((prev) => [created, ...prev]);
      return { success: true };
    } catch (e) {
      setError(e.message);
      return { success: false, error: e.message };
    }
  };

  return (
    <div>
      <div className="form">
        <ActivityForm onCreate={addActivity} />
      </div>

      {loading && <div className="loading">Loading activities...</div>}
      {error && <div className="error">{error}</div>}

      <div className="cards">
        {activities.map((a) => (
          <ActivityCard key={a.id} activity={a} />
        ))}
      </div>
    </div>
  );
}
