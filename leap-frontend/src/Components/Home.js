import React, {Component} from 'react';
import { Link } from 'react-router-dom';


export default class Home extends Component
{
    constructor(props) {
        super(props);
        this.state = {
            };
    }

    componentDidMount() {
    }

    render() {
        return(
            <div class="home-container">
                <div class="home-child">
                <h2>Recently Used Environments</h2>
                <form>
                    <div>
                        <Link to={'/environments'}>
                        <input type="button" value="Recently Used Environments" className="input-button hoverable"/>
                        </Link>
                    </div> 
                </form>
                </div>
                <div class="home-child">
                <h2>Environment</h2>
                <form>
                    <div>
                        <Link to={'/newenvironment'}>
                        <input type="button" value="New Environment" className="input-button hoverable"/>
                        </Link>
                    </div> 
                </form>
                </div>
                <div class="home-child">
                <h2>User List</h2>
                <form>
                    <div>
                        <Link to={'/userlist'}>
                        <input type="button" value="User list" className="input-button hoverable"/>
                        </Link>
                    </div> 
                </form>
                </div>
            </div>
        )
    }








}