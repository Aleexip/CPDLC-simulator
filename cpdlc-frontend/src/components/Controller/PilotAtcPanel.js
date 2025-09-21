function PilotATCPanel({ plane, onAccept, onDeny }) {
  if (!plane)
    return (
      <div
        style={{
          position: "fixed",
          top: 150,
          right: 10,
          zIndex: 1000,
          color: "#fff",
        }}
      >
        Select an aircraft
      </div>
    );

  const pendingCommands =
    plane.commandsHistory?.filter((c) => c.status === "pending") || [];

  return (
    <div
      style={{
        position: "absolute",
        top: 150,
        right: 10,
        zIndex: 1000,
        background: "#222",
        color: "#fff",
        padding: 10,
      }}
    >
      <h4>Pilot Panel - {plane.callsign}</h4>
      {pendingCommands.length === 0 ? (
        <div>No pending commands</div>
      ) : (
        pendingCommands.map((cmd, idx) => (
          <div key={idx} style={{ marginBottom: 5 }}>
            <div>
              {cmd.type}: {cmd.value}
            </div>
            <button onClick={() => onAccept(cmd)}>Accept</button>
            <button onClick={() => onDeny(cmd)}>Deny</button>
          </div>
        ))
      )}
    </div>
  );
}
export default PilotATCPanel;
