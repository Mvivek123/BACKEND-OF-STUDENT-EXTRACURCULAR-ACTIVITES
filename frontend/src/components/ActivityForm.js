import React, { useState } from 'react';

export default function ActivityForm({ onCreate }) {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    setError(null);
    if (!title.trim()) {
      setError('Title is required');
      setSaving(false);
      return;
    }
    const result = await onCreate({ title: title.trim(), description: description.trim() });
    if (!result.success) {
      setError(result.error || 'Failed to create');
    } else {
      setTitle('');
      setDescription('');
    }
    setSaving(false);
  };

  return (
    <form onSubmit={handleSubmit}>
      <input className="input" placeholder="Activity title" value={title} onChange={(e) => setTitle(e.target.value)} />
      <textarea className="input" placeholder="Short description" value={description} onChange={(e) => setDescription(e.target.value)} />
      {error && <div className="error">{error}</div>}
      <button className="button" type="submit" disabled={saving}>{saving ? 'Saving...' : 'Add Activity'}</button>
    </form>
  );
}
