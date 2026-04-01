import React from 'react';

export default function ActivityCard({ activity }) {
  return (
    <div className="card">
      <h3>{activity.title}</h3>
      <p>{activity.description}</p>
      <small>ID: {activity.id}</small>
    </div>
  );
}
