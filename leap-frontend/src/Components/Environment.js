import React, {Component} from 'react';
import { Link } from 'react-router-dom';
import plusImg from '../img/plus.png'



export default class Environment extends Component
{
    constructor(props) {
        super(props);
        this.state = {
            name: '',
            environmentname: props.environmentname,
            capabilities: 0,
            itApplications: 0,
            programs: 0,
            strategies: 0,
            strategyItems: 0,
            projects: 0,
            resources: 0,
            businessProcesses: 0,
            status: 0
        };

    }

    addStatus = () => {
        return '1'
    }

    componentDidMount() {
    }

    render() {
        const environmentName = this.props.match.params.name;
        return(
            <div>
                <nav aria-label="breadcrumb">
                    <ol className="breadcrumb">
                        <li className="breadcrumb-item"><Link to={`/`}><a>Home</a></Link></li>
                        <li className="breadcrumb-item"><Link to={`/environment/${environmentName}`}><a>{environmentName}</a></Link></li>
                    </ol>
                </nav>
                <div className="container jumbotron">
                    <div className="card-deck">
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title text-center">Capabilities</h5>
                                <div className="text-center">
                                <Link to={`${environmentName}/capability/add`}><img src={ plusImg } alt='add' width='30' height='30'/></Link>
                                </div>
                            </div>
                            <div className="card-footer">
                                <Link to={`${environmentName}/capability/all`}><small className="text-muted">{this.state.capabilities} capabilities</small></Link>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title text-center">Strategies</h5>
                                <div className="text-center">
                                    <Link to={`${environmentName}/strategy/add`}><img src={ plusImg } alt='add' width='30' height='30'/></Link>
                                </div>
                            </div>
                            <div className="card-footer">
                                <Link to={`${environmentName}/strategy/all`}><small className="text-muted">{this.state.strategies} strategies</small></Link>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title text-center">Resources</h5>
                                <div className="text-center">
                                    <Link to={`${environmentName}/resource/add`}><img src={ plusImg } alt='add' width='30' height='30'/></Link>
                                </div>
                            </div>
                            <div className="card-footer">
                                <Link to={`${environmentName}/resources/all`}><small className="text-muted">{this.state.resources} resources</small></Link>
                            </div>
                        </div>
                    </div>
                    <div className="card-deck">
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title text-center">IT-Applications</h5>
                                <div className="text-center">
                                    <Link to={`${environmentName}/itapplication/add`}><img src={ plusImg } alt='add' width='30' height='30'/></Link>
                                </div>
                            </div>
                            <div className="card-footer">
                                <Link to={`${environmentName}/itapplication/all`}><small className="text-muted">{this.state.itApplications} IT-applications</small></Link>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title text-center">Strategy Items</h5>
                                <div className="text-center">
                                    <Link to={`${environmentName}/strategyitem/add`}><img src={ plusImg } alt='add' width='30' height='30'/></Link>
                                </div>
                            </div>
                            <div className="card-footer">
                                <Link to={`${environmentName}/strategyitem/all`}><small className="text-muted">{this.state.strategyItems} stategy items</small></Link>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title text-center">Business processes</h5>
                                <div className="text-center">
                                    <Link to={`${environmentName}/businessprocess/add`}><img src={ plusImg } alt='add' width='30' height='30'/></Link>
                                </div>
                            </div>
                            <div className="card-footer">
                                <Link to={`${environmentName}/businessprocess/all`}><small className="text-muted">{this.state.businessProcesses} business processes</small></Link>
                            </div>
                        </div>
                    </div>
                    <div className="card-deck">
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title text-center">Programs</h5>
                                <div className="text-center">
                                    <Link to={`${environmentName}/program/add`}><img src={ plusImg } alt='add' width='30' height='30'/></Link>
                                </div>
                            </div>
                            <div className="card-footer">
                                <Link to={`${environmentName}/programs/all`}><small className="text-muted">{this.state.programs} programs</small></Link>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title text-center">Projects</h5>
                                <div className="text-center">
                                    <Link to={`${environmentName}/project/add`}><img src={ plusImg } alt='add' width='30' height='30'/></Link>
                                </div>
                            </div>
                            <div className="card-footer">
                                <Link to={`${environmentName}/projects/all`}><small className="text-muted">{this.state.projects} projects</small></Link>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title text-center">Status</h5>
                                <div className="text-center">
                                    <Link to={`${environmentName}/status/add`}><img src={ plusImg } alt='add' width='30' height='30'/></Link>
                                </div>
                            </div>
                            <div className="card-footer">
                                <Link to={`${environmentName}/status/all`}><small className="text-muted">{this.state.status} statuses</small></Link>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        )
    }








}