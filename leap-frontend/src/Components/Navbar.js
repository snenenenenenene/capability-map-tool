import { Link } from 'react-router-dom';
import { useAuth0 } from '@auth0/auth0-react';


const Navbar = () => {
    const { logout, user } = useAuth0();
    return(
      <div>
      <nav>   
        <div className="brand">
          <p>LEAP</p>
        </div>
        <Link to={'/'}>
          <button className="hoverable">Home</button>
        </Link>
        <div id="welcome">
          <p>logged in as {user.name}</p>
        </div>
        <div className="login">
          <button className="hoverable" onClick={() => logout({ returnTo: window.location.origin })}>Logout</button>
        </div>
      </nav>
      </div>
    )
}
export default Navbar 