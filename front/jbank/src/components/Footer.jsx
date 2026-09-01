const Footer = () => {
  return (
    <footer className="footer">
      <div className="footer-container">
        <div className="footer-content">
          <div className="footer-section">
            <h3>JBANK</h3>
            <p>Secure banking for the modern world!</p>
          </div>
          <div className="footer-section">
            <h4>Quick links</h4>
            <ul>
              <li>
                <a href="/">Home</a>
              </li>
              <li>
                <a href="/">About</a>
              </li>
              <li>
                <a href="/">Contact</a>
              </li>
            </ul>
          </div>
          <div className="footer-section">
            <h4>Quick links</h4>
            <p>Email: j.beskow16@gmail.com</p>
          </div>
        </div>
        <div className="footer-bottom">
          <p>&copy; {new Date().getFullYear()} JBank. All rights reserverd</p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
